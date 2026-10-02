# ARCHITECTURE BACKEND DE PRODUCTION — ADJAMÉ MARKET
## INFRASTRUCTURE CLOUD FIRESTORE, SERVERLESS & MICRO-SERVICES (B2B AFRIQUE DE L'OUEST)

---

## 1. VUE D'ENSEMBLE DE L'ARCHITECTURE BACKEND

Le backend d'**Adjamé Market** repose sur une architecture Cloud Serverless moderne, hautement disponible et distribuée, optimisée pour le continent africain :

```
                                  [ CLIENTS ]
             (Android Compose / Web App / Future App iOS)
                                       |
                +----------------------+----------------------+
                |                      |                      |
          HTTPS / REST            WSS (Temps Réel)      Firebase App Check
                v                      v                      v
    +-------------------------------------------------------------+
    |                    FIREBASE API GATEWAY                     |
    |         (Authentication, Rate Limiting, CDN Edge)           |
    +-------------------------------------------------------------+
                                       |
         +-----------------------------+-----------------------------+
         |                             |                             |
         v                             v                             v
+------------------+         +-------------------+         +--------------------+
| CLOUD FIRESTORE  |         | CLOUD FUNCTIONS   |         | CLOUD STORAGE      |
| Base Documentaire| <-----> | Microservices TS  | <-----> | Photos de cartons, |
| Temps Réel NoSQL |         | Triggers Métier   |         | Devantures, CNI KYC|
+------------------+         +-------------------+         +--------------------+
         ^                             |                             ^
         |                             v                             |
         |                 +-----------------------+                 |
         |                 | INTÉGRATIONS EXTERNES |                 |
         |                 | - Push FCM & SMS      |                 |
         |                 | - Webhooks Wave/MoMo  |                 |
         |                 | - Moteur Algolia/Meili|                 |
         |                 +-----------------------+                 |
         +-----------------------------+-----------------------------+
```

### Principes Architecturaux :
1. **Zéro Confiance Côté Frontend (Zero-Trust Security) :** Aucune règle de calcul critique (prix total, remises par palier, attribution de statut vérifié) n'est déléguée au client. Tout est calculé et verrouillé par Cloud Functions et Firestore Security Rules.
2. **Immutabilité Transactionnelle :** Les commandes capturent un instantané (snapshot) définitif du prix des articles au moment précis de l'achat pour éviter tout litige en cas de hausse ultérieure du cours par le grossiste.
3. **Résilience Réseau & Faible Débit :** Toutes les écritures Firestore supportent le mode hors-ligne natif avec synchronisation en arrière-plan (Offline Persistence).

---

## 2. SCHÉMA FIRESTORE COMPLET

### 2.1. Collection `users`
*Document ID : `uid` (identique à Firebase Auth)*
- `uid`: `string` [Obligatoire] — Identifiant unique utilisateur.
- `phone`: `string` [Obligatoire] — Numéro de téléphone au format E.164 (+225XXXXXXXXXX).
- `role`: `string` [Obligatoire] — `'BUYER'` | `'WHOLESALER'` | `'ADMIN'`.
- `fullName`: `string` [Obligatoire] — Nom du gérant ou commerçant.
- `businessName`: `string` [Facultatif] — Enseigne commerciale.
- `commune`: `string` [Obligatoire] — Commune ou ville de rattachement (ex: Yopougon, Bouaké).
- `email`: `string` [Facultatif] — Adresse e-mail de contact.
- `avatarUrl`: `string` [Facultatif] — URL Cloud Storage de la photo de profil.
- `fcmTokens`: `array<string>` [Facultatif] — Jetons de push notifications pour terminaux mobiles.
- `isVerified`: `boolean` [Obligatoire, défaut: false] — Certification par la plateforme.
- `accountStatus`: `string` [Obligatoire, défaut: 'ACTIVE'] — `'ACTIVE'` | `'SUSPENDED'` | `'BANNED'`.
- `createdAt`: `timestamp` [Obligatoire, serveur].
- `updatedAt`: `timestamp` [Obligatoire, serveur].
- **Index :** `phone ASC`, `role ASC, createdAt DESC`.

### 2.2. Collection `shops` (Boutiques Grossistes)
*Document ID : `shopId` (généré automatiquement)*
- `shopId`: `string` [Obligatoire].
- `ownerId`: `string` [Obligatoire] — Référence vers `users.uid`.
- `name`: `string` [Obligatoire] — Nom commercial de la boutique au marché.
- `description`: `string` [Obligatoire] — Présentation de l'activité de gros.
- `marketSector`: `string` [Obligatoire] — Sous-marché (*Forum d'Adjamé, Black Market, Gouro, Roxy, Dallas*).
- `address`: `string` [Obligatoire] — Emplacement physique précis (ex: *Forum Niveau 1, Magasin B-14*).
- `landmarks`: `string` [Obligatoire] — Repères ivoiriens (*Face pharmacie Mirador, couloir B*).
- `phone`: `string` [Obligatoire].
- `whatsapp`: `string` [Obligatoire] — Numéro WhatsApp pour commande directe.
- `hours`: `string` [Défaut: "7h30 - 18h00"].
- `categoryIds`: `array<string>` [Obligatoire] — Liste des filières proposées.
- `isVerified`: `boolean` [Défaut: false] — Attribué uniquement par l'Admin.
- `verificationBadge`: `string` [Défaut: 'STANDARD'] — `'TERRAIN_VERIFIED'` | `'CERTIFIED_IMPORTATEUR'` | `'STANDARD'`.
- `rating`: `number` [Défaut: 5.0] — Note moyenne calculée par Cloud Function.
- `reviewCount`: `number` [Défaut: 0] — Total des avis certifiés.
- `transactionCount`: `number` [Défaut: 0] — Total des commandes retirées avec succès.
- `averageResponseMinutes`: `number` [Défaut: 15] — Délai moyen de réponse.
- `geopoint`: `geopoint` [Facultatif] — Latitude et longitude pour calcul de proximité.
- `isActive`: `boolean` [Défaut: true].
- `coverImageUrl`: `string` [Facultatif].
- `createdAt`: `timestamp`.
- **Index :** `marketSector ASC, isVerified DESC`, `categoryIds ARRAY_CONTAINS`, `rating DESC`.

### 2.3. Collection `products` (Articles au Carton & Paliers B2B)
*Document ID : `productId` (généré)*
- `productId`: `string` [Obligatoire].
- `shopId`: `string` [Obligatoire] — Rattaché à la boutique.
- `sellerId`: `string` [Obligatoire] — Rattaché au grossiste propriétaire.
- `categoryId`: `string` [Obligatoire] — Référence vers `categories.id`.
- `name`: `string` [Obligatoire] — Désignation commerciale (ex: *Savon Kanza Éclaircissant*).
- `searchKeywords`: `array<string>` [Obligatoire] — Mots-clés normalisés en minuscules pour recherche tolérante.
- `description`: `string` [Obligatoire].
- `packaging`: `string` [Obligatoire] — *Carton de 24 pièces, Douzaine, Ballot, Sac 50kg*.
- `minOrderQuantity`: `number` [Obligatoire, $\ge 1$] — MOQ obligatoire.
- `basePrice`: `number` [Obligatoire] — Prix indicatif au carton en FCFA.
- `priceTiers`: `array<map>` [Facultatif] :
  - `minQuantity`: `number`
  - `maxQuantity`: `number | null`
  - `unitPriceFcfa`: `number`
  - `label`: `string`
- `stockStatus`: `string` [Obligatoire] — `'IN_STOCK'` | `'LOW_STOCK'` | `'ARRIVAGE'` | `'OUT_OF_STOCK'`.
- `stockQuantity`: `number` [Obligatoire, défaut: 50] — Nombre de cartons en réserve.
- `images`: `array<string>` [Obligatoire, 1 à 5 photos].
- `isPromoted`: `boolean` [Défaut: false] — Mise en avant payante "Arrivage Chaud".
- `createdAt`: `timestamp`.
- `updatedAt`: `timestamp`.
- **Index :** `categoryId ASC, isPromoted DESC, basePrice ASC`, `shopId ASC, createdAt DESC`, `searchKeywords ARRAY_CONTAINS`.

### 2.4. Collection `categories`
*Document ID : `cat_xxx`*
- `id`: `string` [Obligatoire].
- `name`: `string` [Obligatoire] (ex: *Beauté & Cosmétiques*).
- `slug`: `string` [Obligatoire].
- `iconKey`: `string` [Obligatoire].
- `description`: `string` [Obligatoire].
- `displayOrder`: `number` [Obligatoire].
- `productCount`: `number` [Défaut: 0].

### 2.5. Collection `orders` (Commandes & Réservations B2B)
*Document ID : `orderId` (ex: `CMD-9481`)*
- `orderId`: `string` [Obligatoire].
- `buyerId`: `string` [Obligatoire] — `uid` du revendeur.
- `buyerName`: `string` [Obligatoire].
- `buyerPhone`: `string` [Obligatoire].
- `sellerId`: `string` [Obligatoire] — `uid` du grossiste.
- `shopId`: `string` [Obligatoire].
- `sellerName`: `string` [Obligatoire].
- `sellerSector`: `string` [Obligatoire].
- `items`: `array<map>` [Obligatoire] :
  - `productId`: `string`
  - `productName`: `string`
  - `packaging`: `string`
  - `quantity`: `number`
  - `unitPrice`: `number` (Prix historique figé)
  - `subtotal`: `number`
- `totalAmount`: `number` [Obligatoire] — Somme totale en FCFA.
- `status`: `string` [Obligatoire] :
  `'PENDING'` $\rightarrow$ `'CONFIRMED'` $\rightarrow$ `'PREPARING'` $\rightarrow$ `'READY'` $\rightarrow$ `'COMPLETED'` (ou `'CANCELLED'`, `'REJECTED'`).
- `pickupPinCode`: `string` [Obligatoire] — Code à 4 chiffres (ex: `"7482"`).
- `deliveryType`: `string` [Obligatoire] — `'CLICK_AND_COLLECT'` | `'GARE_EXPEDITION'`.
- `paymentStatus`: `string` [Défaut: 'UNPAID'] — `'UNPAID'` | `'ESCROW_LOCKED'` | `'PAID'`.
- `paymentProvider`: `string` [Facultatif] — `'WAVE'` | `'ORANGE_MONEY'` | `'MTN_MOMO'`.
- `paymentTransactionId`: `string` [Facultatif].
- `notes`: `string` [Facultatif] — Consignes de fret ou de gare.
- `createdAt`: `timestamp`.
- `updatedAt`: `timestamp`.
- **Index :** `buyerId ASC, createdAt DESC`, `sellerId ASC, status ASC, createdAt DESC`.

### 2.6. Collections Complémentaires
- **`conversations` & sous-collection `messages` :**
  - `participantIds`: `array<string>`
  - `lastMessage`: `string`, `lastMessageTime`: `timestamp`, `unreadCounts`: `map<uid, number>`.
- **`reviews` :**
  - `shopId`: `string`, `orderId`: `string`, `authorId`: `string`, `authorName`: `string`, `rating`: `number (1-5)`, `comment`: `string`, `createdAt`: `timestamp`.
- **`verificationRequests` (Audit Terrain KYC) :**
  - `wholesalerId`: `string`, `shopId`: `string`, `idDocumentUrl`: `string`, `businessProofUrl`: `string`, `status`: `'REQUESTED' | 'UNDER_REVIEW' | 'VERIFIED' | 'REJECTED'`, `auditorAgentName`: `string`, `notes`: `string`.
- **`reports` (Litiges & Fraudes) :**
  - `shopId`: `string`, `reporterId`: `string`, `reason`: `string`, `description`: `string`, `evidencePhotoUrls`: `array<string>`, `status`: `'PENDING' | 'RESOLVED' | 'DISMISSED'`.
- **`auditLogs` :**
  - Registre immuable des actions d'administration (`SHOP_VERIFIED`, `SHOP_BANNED`, `PRICE_MODERATED`).

---

## 3. RÈGLES DE SÉCURITÉ (FIRESTORE & STORAGE)

*Les fichiers de règles complets sont compilés et enregistrés sous `/backend/firestore.rules` et `/backend/storage.rules`.*

### Points Clés de Sécurité :
1. **Interdiction de falsification de rôle :** Un utilisateur ne peut jamais s'auto-attribuer le rôle `ADMIN` lors de son inscription.
2. **Propriété exclusive de boutique :** Un grossiste ne peut modifier ou supprimer que ses propres fiches produits et stocks.
3. **Verrouillage des commandes :** Seul le grossiste destinataire peut faire progresser les étapes de préparation ; seul l'acheteur ou le grossiste peut annuler avant confirmation.
4. **Anti-Avis Fantôme :** Le système vérifie l'existence d'une commande `COMPLETED` liée avant d'autoriser la publication d'une note.
5. **Protection stricte des pièces KYC :** Les pièces d'identité et baux commerciaux stockés sur Cloud Storage ne sont lisibles que par le grossiste déposant et l'équipe d'administration.

---

## 4. CLOUD FUNCTIONS NÉCESSAIRES (SERVERLESS ENGINE)

*Le code TypeScript source complet est implémenté sous `/backend/functions/src/index.ts`.*

1. **`onOrderCreated` (Triggers Firestore) :**
   - Exécute une transaction atomique pour décrémenter le stock dans `products`.
   - Si le stock est insuffisant, rejette immédiatement la commande sans débiter le client.
   - Pousse une notification haute priorité FCM au grossiste avec sonnerie d'ambiance bruyante.
2. **`onOrderStatusChanged` (Triggers Firestore) :**
   - Alerte l'acheteur à chaque avancée (notamment lorsque la commande est prête, avec rappel du code PIN à 4 chiffres).
3. **`onReviewCreated` (Agrégation de Données) :**
   - Recalcule la note moyenne et le nombre d'avis sur le document `shops/{shopId}` de façon concurrente et sûre via transaction.
4. **`onVerificationRequestUpdated` (Audit Terrain) :**
   - Active instantanément le badge doré "Boutique Vérifiée" dès validation par l'agent de terrain et consigne l'événement dans `auditLogs`.
5. **`handleMobileMoneyWebhook` (Passerelle Paiement HTTPS) :**
   - Valide les callbacks IPN signés par HMAC (Wave, Orange Money, MTN MoMo), sécurise le paiement en séquestre et confirme la commande.

---

## 5. SYSTÈME DE NOTIFICATIONS MULTICANAL

Les commerçants d'Adjamé évoluant dans un environnement sonore dense avec des coupures de data intermittentes, la stratégie de notification est hybride :

1. **Canal Primaire (FCM Push) :**
   - Notifications haute priorité Android (`priority: "high"`) avec canal dédié `orders_urgent_channel`.
   - Sonnerie personnalisée forte enregistrée dans les ressources locales de l'application.
2. **Canal Secondaire (WhatsApp Business API & SMS Fallback) :**
   - Si une commande n'a pas été ouverte par le grossiste sous 15 minutes, une tâche Cloud Tasks déclenche un SMS court ou une alerte WhatsApp :
     *« Adjamé Market : Vous avez une nouvelle commande CMD-XXXX en attente de préparation. Ouvrez votre application. »*

---

## 6. WORKFLOW DE VÉRIFICATION TERRAIN (KYC GROSSISTE)

```
[Grossiste inscrit] 
        | (Soumet CNI + Facture CIE/SODECI)
        v
[Demande status = 'REQUESTED'] 
        | (Planification de la tournée terrain)
        v
[Visite de l'Ambassadeur Adjamé Market au Forum / Roxy / Dallas]
        | - Contrôle physique du stock
        | - Prise de photo géomarquée de l'enseigne
        | - Pose du sticker officiel avec QR code
        v
[Validation Admin sur la Console : status = 'VERIFIED']
        |
        v
[Mise à jour atomique du badge doré sur la boutique + Notification push]
```

---

## 7. MODÈLE TRANSACTIONNEL DE COMMANDE & ANTI-FRAUDE

1. **Snapshot de Prix :**
   Le prix unitaire appliqué au moment de l'ajout au panier est revérifié côté serveur par la Cloud Function avant validation finale. Une fois la commande enregistrée, ce prix devient immuable.
2. **Séquestre Mobile Money (Escrow Optionnel) :**
   Les fonds versés par l'acheteur via Wave ou Orange Money sont bloqués sur un compte séquestre technique.
3. **Délivrance par Code PIN :**
   L'acheteur ou son livreur remet au grossiste le code PIN à 4 chiffres généré par l'application. Dès que le grossiste saisit ce code sur son téléphone, la commande passe en `COMPLETED` et les fonds sont débloqués.

---

## 8. STRATÉGIE DE RECHERCHE SCALABLE

Pour absorber une croissance de 1 000 à 100 000 produits :

1. **Phase 1 (MVP Firestore Native) :**
   - Normalisation des mots-clés dans le tableau `searchKeywords` de chaque produit (*ex: `["savon", "kanza", "eclaircissant", "anti-taches"]`*).
   - Requête efficace : `products.where("searchKeywords", "array-contains", queryWord)`.
2. **Phase 2 (Moteur Dédié Algolia / Meilisearch / Typesense) :**
   - Cloud Function `onProductWritten` qui indexe en temps réel chaque création ou modification de produit vers un cluster de recherche hébergé.
   - Supporte la tolérance aux fautes de frappe (*"savong"* trouve *"savon"*), le classement par popularité et la recherche géo-spatiale par rayon kilométrique (GeoRadius).

---

## 9. STRATÉGIE DE SAUVEGARDE & DISASTER RECOVERY

1. **Sauvegardes Quotidiennes Automatisées :**
   - Tâche Cloud Scheduler déclenchant chaque nuit un export managé de Firestore vers un bucket Cloud Storage multi-région :
     `gcloud firestore export gs://adjame-market-backups/daily/$(date +%Y-%m-%d)`
2. **Point-in-Time Recovery (PITR) :**
   - Activation du PITR Firestore permettant la restauration de la base à la seconde près sur les 7 derniers jours en cas de suppression accidentelle.
3. **Rétention des Documents :**
   - Suppression logique (`isDeleted: true`) privilégiée pour conserver l'intégrité référentielle des commandes et des statistiques fiscales.

---

## 10. MONITORING, OBSERVABILITÉ & ÉVOLUTIVITÉ

1. **Alerting Cloud Monitoring :**
   - Alertes instantanées sur canal Slack/WhatsApp de l'équipe technique en cas de :
     - Taux d'erreur Cloud Functions > 1%.
     - Latence moyenne de lecture Firestore > 250ms.
     - Échec de livraison de webhooks de paiement Mobile Money.
2. **Compteurs Shardés (Distributed Counters) :**
   - Pour les produits à très fort trafic (arrivage conteneur flash), les compteurs de vues et de stocks sont distribués sur 10 sous-documents pour éliminer la limite d'1 écriture/seconde de Firestore.
3. **Haute Disponibilité :**
   - Déploiement multi-région `europe-west` ou `africa-south` garantissant une disponibilité de 99.999%.

---

*L'ensemble des fichiers de règles et du code de backend est opérationnel et versionné dans les répertoires `/backend/firestore.rules`, `/backend/storage.rules` et `/backend/functions/src/index.ts`.*
