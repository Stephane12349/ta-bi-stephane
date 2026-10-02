# PLAN DE DÉPLOIEMENT, PERFORMANCE, MONITORING & FINOPS (DEVOPS)
## ADJAMÉ MARKET — INFRASTRUCTURE SCALE 10 000 UTILISATEURS SIMULTANÉS & RÉSEAU 3G/4G AFRIQUE DE L'OUEST

---

## 1. OBJECTIFS DE PERFORMANCE & CONTRAINTES INFRASTRUCTURE

* **Pic d'utilisateurs simultanés :** 10 000 revendeurs et grossistes connectés (heures de pointe d'Adjamé : 07h00 - 11h30 et 16h00 - 19h00).
* **Conditions réseau terrain :** Connectivité mobile hétérogène (3G/4G Orange, MTN, Moov CI) avec paquets perdus, gigue et débits fluctuant entre 256 kbps et 15 Mbps.
* **Budget Cloud maîtrisé (FinOps) :** Éviter l'explosion des lectures/écritures Firestore lors des pics de consultation du catalogue.

---

## 2. MODÉLISATION DE CHARGE & OPTIMISATION FIRESTORE

### 2.1. Calcul de Charge (10 000 Concurrences)
* **Recherche & Consultation :** 8 000 sessions actives effectuant 5 requêtes de recherche/filtrage par minute = 40 000 requêtes/minute (~667 req/s).
* **Ajout panier & Demandes :** 1 500 opérations/minute.
* **Passage de commande :** 200 commandes/minute.

### 2.2. Architecture FinOps anti-surcoût Firestore
1. **Cache Local Room Décentralisé (Tier 0) :**
   * Au lancement, l'application vérifie l'empreinte `version_etag` ou l'horodatage de mise à jour des catégories et grossistes vérifiés.
   * Si inchangé, 0 lecture Firestore : l'utilisateur navigue à 100% dans la base SQLite locale Room (0 milliseconde de latence réseau).
2. **Bundle Cache Firestore (Tier 1) :**
   * Utilisation des **Firestore Data Bundles** pré-sérialisés générés par Cloud Functions chaque heure pour les "Top Produits du Forum" et "Nouveaux Arrivages".
   * Le client télécharge un seul fichier binaire compact mis en cache HTTP (Cloud CDN), réduisant les lectures de documents individuels de 85%.
3. **Partitionnement & Sharding des Compteurs :**
   * Pour les compteurs à haute fréquence (ex: nombre de vues d'un produit vedette, nombre de clics WhatsApp), utilisation d'un **Distributed Counter** (10 shards sous-jacents) pour respecter la limite Firestore de 1 écriture/seconde par document.

---

## 3. INDEXATION FIRESTORE MULTI-CRITÈRES

Pour supporter les requêtes combinées sans scans de collections coûteux :

```json
{
  "indexes": [
    {
      "collectionGroup": "products",
      "queryScope": "COLLECTION",
      "fields": [
        { "fieldPath": "categoryId", "order": "ASCENDING" },
        { "fieldPath": "stockStatus", "order": "ASCENDING" },
        { "fieldPath": "basePrice", "order": "ASCENDING" }
      ]
    },
    {
      "collectionGroup": "products",
      "queryScope": "COLLECTION",
      "fields": [
        { "fieldPath": "sellerSector", "order": "ASCENDING" },
        { "fieldPath": "isVerifiedSeller", "order": "DESCENDING" },
        { "fieldPath": "createdAt", "order": "DESCENDING" }
      ]
    },
    {
      "collectionGroup": "orders",
      "queryScope": "COLLECTION",
      "fields": [
        { "fieldPath": "sellerId", "order": "ASCENDING" },
        { "fieldPath": "status", "order": "ASCENDING" },
        { "fieldPath": "createdAt", "order": "DESCENDING" }
      ]
    }
  ]
}
```

---

## 4. GESTION DES PIC DE COMMANDES & LATENCE 3G/4G

1. **Transactions Atomiques Sécurisées :**
   * Tout passage de commande décrémentant le stock est encapsulé dans `firestore.runTransaction()`.
   * En cas de conflit de concurrence sur le dernier carton, la transaction relance automatiquement jusqu'à 5 fois avant de notifier l'épuisement.
2. **Optimistic UI Updates :**
   * L'acheteur voit son action confirmée immédiatement à l'écran grâce à la réactivité locale StateFlow / Room.
   * L'appel réseau s'effectue en arrière-plan avec retry exponentiel (Exponential Backoff).
3. **Compression d'Images WebP & Thumbnails :**
   * Tout visuel soumis par un grossiste est redimensionné côté client (max 800px) et compressé en WebP à 75% avant envoi vers Firebase Storage.
   * Économie de bande passante : réduction du poids moyen par image de 3.5 Mo à ~85 Ko.

---

## 5. OBSERVABILITÉ, MONITORING & ALERTING DEVOPS

| Composant | Outil | Métrique Clé Surveillée | Seuil d'Alerte |
| :--- | :--- | :--- | :--- |
| **API & Cloud Functions** | Google Cloud Monitoring | Temps de réponse p95, taux d'erreur HTTP 5xx | > 1 200 ms ou Erreurs > 1% |
| **Firestore** | Cloud Logging & Quotas | Ratios d'opérations Lecture/Écriture, document contention | > 50 000 reads/sec simultanés |
| **Application Mobile** | Firebase Crashlytics | Crash-free user rate, gel de l'écran (ANR) | Crash-free < 99.5% ou ANR > 0.2% |
| **Performance Réseau** | Firebase Performance | Latence des requêtes réseau par opérateur (Orange vs MTN) | Durée moyenne > 2.5s sur les routes critiques |
| **FinOps Cloud** | Google Cloud Billing Alerts | Dépassement prévisionnel budget mensuel | Alertes à 50%, 80%, 100% (Budget cible : 250 $/mois en phase 1) |

---
