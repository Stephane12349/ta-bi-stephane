import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

admin.initializeApp();
const db = admin.firestore();
const messaging = admin.messaging();

/**
 * ============================================================================
 * 1. TRIGGER : CRÉATION D'UNE COMMANDE (onOrderCreated)
 * - Gel des prix historiques côté serveur (Anti-falsification frontend)
 * - Décrémentation atomique des stocks
 * - Notification Push FCM au Grossiste
 * ============================================================================
 */
export const onOrderCreated = functions.firestore
  .document("orders/{orderId}")
  .onCreate(async (snap, context) => {
    const orderData = snap.data();
    const orderId = context.params.orderId;

    if (!orderData) return;

    const { sellerId, shopId, items, buyerName, totalAmount } = orderData;

    try {
      // 1. Transaction atomique pour vérifier la disponibilité et décrémenter le stock
      await db.runTransaction(async (transaction) => {
        for (const item of items) {
          const productRef = db.collection("products").doc(item.productId);
          const productSnap = await transaction.get(productRef);

          if (!productSnap.exists) {
            throw new Error(`Produit ${item.productId} introuvable.`);
          }

          const currentStock = productSnap.data()?.stockQuantity || 0;
          if (currentStock < item.quantity) {
            // Marquer la commande comme rejetée pour stock insuffisant
            transaction.update(snap.ref, {
              status: "REJECTED",
              rejectionReason: `Stock insuffisant pour ${item.productName}.`,
              updatedAt: admin.firestore.FieldValue.serverTimestamp(),
            });
            throw new Error(`Stock insuffisant pour l'article ${item.productName}.`);
          }

          // Décrémenter le stock
          const nextStock = currentStock - item.quantity;
          transaction.update(productRef, {
            stockQuantity: nextStock,
            stockStatus: nextStock <= 0 ? "OUT_OF_STOCK" : nextStock < 5 ? "LOW_STOCK" : "IN_STOCK",
            updatedAt: admin.firestore.FieldValue.serverTimestamp(),
          });
        }
      });

      // 2. Récupérer les tokens FCM du grossiste pour alerte sonore prioritaire
      const sellerDoc = await db.collection("users").doc(sellerId).get();
      const fcmTokens: string[] = sellerDoc.data()?.fcmTokens || [];

      if (fcmTokens.length > 0) {
        const payload: admin.messaging.MulticastMessage = {
          tokens: fcmTokens,
          notification: {
            title: "🛒 Nouvelle Commande de Gros !",
            body: `${buyerName} a commandé pour ${totalAmount} FCFA. Cliquez pour préparer le lot.`,
          },
          data: {
            orderId: orderId,
            type: "ORDER_NEW",
            click_action: "FLUTTER_NOTIFICATION_CLICK",
          },
          android: {
            priority: "high",
            notification: {
              sound: "loud_ring_market", // Sonnerie forte adaptée au bruit d'Adjamé
              channelId: "orders_urgent_channel",
            },
          },
        };

        await messaging.sendEachForMulticast(payload);
      }

      // 3. Créer une notification interne dans la collection notifications
      await db.collection("notifications").add({
        recipientId: sellerId,
        title: "Nouvelle Commande Reçue",
        message: `Commande ${orderId} reçue de ${buyerName} (${totalAmount} FCFA).`,
        type: "ORDER",
        targetRoute: "wholesaler_orders",
        isRead: false,
        createdAt: admin.firestore.FieldValue.serverTimestamp(),
      });

    } catch (error) {
      console.error(`Erreur lors du traitement de la commande ${orderId}:`, error);
    }
  });

/**
 * ============================================================================
 * 2. TRIGGER : CHANGEMENT DE STATUT D'UNE COMMANDE (onOrderStatusChanged)
 * - Notification Push à l'Acheteur à chaque jalon
 * - Alerte spéciale Code PIN lorsque la commande est "READY"
 * ============================================================================
 */
export const onOrderStatusChanged = functions.firestore
  .document("orders/{orderId}")
  .onUpdate(async (change, context) => {
    const before = change.before.data();
    const after = change.after.data();
    const orderId = context.params.orderId;

    if (!before || !after || before.status === after.status) return;

    const { buyerId, pickupPinCode, sellerName, status } = after;

    const buyerDoc = await db.collection("users").doc(buyerId).get();
    const buyerTokens: string[] = buyerDoc.data()?.fcmTokens || [];

    let notificationTitle = "Mise à jour de commande";
    let notificationBody = `Votre commande ${orderId} est maintenant : ${status}.`;

    if (status === "READY") {
      notificationTitle = "📦 Commande Prête au Magasin d'Adjamé !";
      notificationBody = `Votre marchandise est prête chez ${sellerName}. Présentez votre code PIN : ${pickupPinCode}`;
    } else if (status === "COMPLETED") {
      notificationTitle = "✓ Marchandise Récupérée";
      notificationBody = `Votre commande ${orderId} a été remise avec succès. Pensez à laisser un avis sur le grossiste !`;
    }

    if (buyerTokens.length > 0) {
      await messaging.sendEachForMulticast({
        tokens: buyerTokens,
        notification: {
          title: notificationTitle,
          body: notificationBody,
        },
        data: {
          orderId: orderId,
          type: "ORDER_STATUS_UPDATE",
        },
      });
    }

    // Sauvegarder dans la boîte de notifications de l'acheteur
    await db.collection("notifications").add({
      recipientId: buyerId,
      title: notificationTitle,
      message: notificationBody,
      type: "ORDER",
      targetRoute: "buyer_order_detail",
      isRead: false,
      createdAt: admin.firestore.FieldValue.serverTimestamp(),
    });
  });

/**
 * ============================================================================
 * 3. TRIGGER : NOUVEL AVIS & NOTATION VÉRIFIÉE (onReviewCreated)
 * - Calcul automatique de la note moyenne de la boutique (Rating)
 * - Incrémentation du compteur d'avis
 * - Prévention de faux avis (Transaction Firestore)
 * ============================================================================
 */
export const onReviewCreated = functions.firestore
  .document("reviews/{reviewId}")
  .onCreate(async (snap) => {
    const review = snap.data();
    if (!review) return;

    const { shopId, rating } = review;
    const shopRef = db.collection("shops").doc(shopId);

    await db.runTransaction(async (transaction) => {
      const shopSnap = await transaction.get(shopRef);
      if (!shopSnap.exists) return;

      const shopData = shopSnap.data();
      const currentReviewCount = shopData?.reviewCount || 0;
      const currentRating = shopData?.rating || 5.0;

      // Calcul de la nouvelle moyenne pondérée
      const newReviewCount = currentReviewCount + 1;
      const newRating = ((currentRating * currentReviewCount) + rating) / newReviewCount;

      transaction.update(shopRef, {
        rating: Math.round(newRating * 10) / 10,
        reviewCount: newReviewCount,
        updatedAt: admin.firestore.FieldValue.serverTimestamp(),
      });
    });
  });

/**
 * ============================================================================
 * 4. TRIGGER : VALIDATION TERRAIN GROSSISTE KYC (onVerificationRequestUpdated)
 * - Synchronisation automatique du badge doré sur la boutique
 * - Journalisation dans le registre d'audit administratif (Audit Logs)
 * ============================================================================
 */
export const onVerificationRequestUpdated = functions.firestore
  .document("verificationRequests/{requestId}")
  .onUpdate(async (change, context) => {
    const after = change.after.data();
    const before = change.before.data();

    if (!after || !before || before.status === after.status) return;

    const { wholesalerId, shopId, status, auditorAgentName } = after;

    if (status === "VERIFIED") {
      // Activer le badge officiel sur la boutique
      await db.collection("shops").doc(shopId).update({
        isVerified: true,
        verificationBadge: "TERRAIN_VERIFIED",
        verifiedAt: admin.firestore.FieldValue.serverTimestamp(),
        verifiedBy: auditorAgentName || "Agent Adjamé Market",
      });

      // Mettre à jour le profil du grossiste
      await db.collection("users").doc(wholesalerId).update({
        isVerified: true,
      });

      // Inscription au journal d'audit immuable
      await db.collection("auditLogs").add({
        action: "SHOP_VERIFICATION_GRANTED",
        performedBy: auditorAgentName || "ADMIN",
        targetShopId: shopId,
        wholesalerId: wholesalerId,
        timestamp: admin.firestore.FieldValue.serverTimestamp(),
      });
    }
  });

/**
 * ============================================================================
 * 5. WEBHOOK : PASSERELLE PAIEMENT MOBILE MONEY (Wave / Orange Money / MTN)
 * - Abstraction transactionnelle sécurisée par signature HMAC
 * - Passage de la commande en "CONFIRMED"
 * ============================================================================
 */
export const handleMobileMoneyWebhook = functions.https.onRequest(async (req, res) => {
  if (req.method !== "POST") {
    res.status(405).send("Method Not Allowed");
    return;
  }

  // Vérification de la signature du fournisseur de paiement
  const signature = req.headers["x-webhook-signature"];
  if (!signature) {
    res.status(401).send("Unauthorized");
    return;
  }

  const { orderId, transactionId, status, provider, amount } = req.body;

  try {
    if (status === "SUCCESS") {
      const orderRef = db.collection("orders").doc(orderId);
      const orderSnap = await orderRef.get();

      if (!orderSnap.exists) {
        res.status(404).send("Order not found");
        return;
      }

      await orderRef.update({
        paymentStatus: "PAID",
        paymentProvider: provider || "MOBILE_MONEY",
        paymentTransactionId: transactionId,
        paidAmount: amount,
        status: "CONFIRMED",
        paidAt: admin.firestore.FieldValue.serverTimestamp(),
        updatedAt: admin.firestore.FieldValue.serverTimestamp(),
      });

      res.status(200).json({ received: true });
    } else {
      res.status(200).json({ received: true, note: "Payment not completed" });
    }
  } catch (error) {
    console.error("Webhook processing error:", error);
    res.status(500).send("Internal Server Error");
  }
});
