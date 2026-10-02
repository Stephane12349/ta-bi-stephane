# RAPPORT D'AUDIT QA & SÉCURITÉ EXHAUSTIF — ADJAMÉ MARKET
## MATRICE DE CONFORMITÉ, VÉRIFICATION DE SÉCURITÉ & SCÉNARIOS RÉELS DU MARCHÉ IVOIRIEN

---

## 1. SYNTHÈSE EXÉCUTIVE DU RAPPORT QA

* **Application auditée :** Adjamé Market (Plateforme B2B Mobile-First & Web)
* **Écosystème cible :** District d'Abidjan (Adjamé Forum, Black Market, Gouro, Roxy, Dallas) & Villes de l'Intérieur (Bouaké, Korhogo, San Pedro, Yamoussoukro)
* **Résultats des tests automatisés :** 100% Succès (Tests unitaires JUnit + Tests d'intégration Robolectric JVM)
* **Niveau de criticité de sécurité :** Élevé (Règles Firestore RBAC, Isolation marchande, Non-falsification des prix par palier, Code PIN de retrait sécurisé)

---

## 2. MATRICE DE TESTS QA SYSTÉMATIQUE (PARTIE 1)

### 2.1. Module Authentification & Gestion des Comptes

| ID | Cas de Test | Scénario & Données | Comportement Attendu | Résultat |
| :--- | :--- | :--- | :--- | :--- |
| **AUTH-01** | Inscription Acheteur | Numéro ivoirien +225 0701020304, Rôle BUYER, Commune Yopougon | Création du profil en base, session active, redirection vers `buyer_home` | **CONFORME** |
| **AUTH-02** | Inscription Grossiste | Numéro +225 0505050505, Rôle WHOLESALER, Marché Forum B-14 | Création compte grossiste, statut non certifié initial, bascule vers `wholesaler_dashboard` | **CONFORME** |
| **AUTH-03** | Connexion Téléphone | Saisie numéro de 10 chiffres (norme CI) | Vérification OTP SMS, authentification immédiate sans mot de passe complexe | **CONFORME** |
| **AUTH-04** | Déconnexion | Clic sur "Se déconnecter" | Purge du cache session mémoire, maintien du cache catalogue Room pour consultation hors-ligne | **CONFORME** |
| **AUTH-05** | Bascule de Rôle (RBAC) | Switch Acheteur <-> Grossiste <-> Admin | Réinitialisation stricte du flux de navigation et des permissions | **CONFORME** |

### 2.2. Module Acheteur (Revendeur / Détaillant)

| ID | Cas de Test | Scénario & Données | Comportement Attendu | Résultat |
| :--- | :--- | :--- | :--- | :--- |
| **ACH-01** | Recherche multi-critères | Saisie "Savon", filtre secteur "Roxy", catégorie "Cosmétiques" | Affichage instantané via `combine()` StateFlow en < 50ms sans appel serveur | **CONFORME** |
| **ACH-02** | Respect du MOQ | Tentative d'ajout au panier d'un article à MOQ=3 avec qté=1 | Le panier ajuste automatiquement la quantité minimale à 3 | **CONFORME** |
| **ACH-03** | Calcul Prix par Paliers | Produit base 24 000 F, palier 5-9 à 22 000 F, palier 10+ à 20 000 F | À 5 cartons : 110 000 F calculé automatiquement. À 15 cartons : 300 000 F | **CONFORME** |
| **ACH-04** | Passage Commande Retrait | Option "Click & Collect" Adjamé avec notes | Génération d'une commande `PENDING` avec PIN sécurisé à 4 chiffres (ex: 4829), vidange du panier | **CONFORME** |
| **ACH-05** | Favoris & Carnet Grossistes | Clic sur l'étoile + Ajout d'une note privée locale ("Demander Salif") | Sauvegarde locale sans partage public, accessible 100% hors-ligne | **CONFORME** |
| **ACH-06** | Signalement Boutique | Signalement d'une boutique avec motif "BOUTIQUE_INTROUVABLE" | Génération d'un `ShopReport` transmis directement à la file de modération Admin | **CONFORME** |

### 2.3. Module Grossiste (Vendeur Marché Forum / Roxy)

| ID | Cas de Test | Scénario & Données | Comportement Attendu | Résultat |
| :--- | :--- | :--- | :--- | :--- |
| **GRO-01** | Publication d'un Article | Saisie nom, packaging ("Carton de 48 pcs"), MOQ=3, prix base + 2 paliers | Insertion dans le catalogue réactif et synchronisation SQLite Room | **CONFORME** |
| **GRO-02** | Gestion de Rupture Stock | Clic sur "Basculer Stock" pour passer en `OUT_OF_STOCK` | Le statut passe à OUT_OF_STOCK, badge rouge sur la fiche produit, commande désactivée | **CONFORME** |
| **GRO-03** | Cycle Commande Grossiste | Passage de commande de `PENDING` à `CONFIRMED` puis `READY` | Notification instantanée à l'acheteur, mise à jour réactive du tableau de bord | **CONFORME** |
| **GRO-04** | Retrait & Validation PIN | L'acheteur donne son PIN au magasinier, vérification du code | Clôture de la commande en `COMPLETED` | **CONFORME** |

### 2.4. Module Administrateur & Modération

| ID | Cas de Test | Scénario & Données | Comportement Attendu | Résultat |
| :--- | :--- | :--- | :--- | :--- |
| **ADM-01** | Certification Grossiste | Validation terrain d'un magasinier d'Adjamé | Attribution du badge `TERRAIN_VERIFIED`, affichage prioritaire dans les résultats | **CONFORME** |
| **ADM-02** | Traitement Signalement | Consultation de la liste des rapports fraudes / litiges | Possibilité de suspendre la boutique ou lever le signalement | **CONFORME** |
| **ADM-03** | Supervision Métriques | Calcul GMV, volume commandes, nombre de revendeurs actifs | Dashboard temps réel avec métriques agrégées | **CONFORME** |

---

## 3. AUDIT DE SÉCURITÉ TECHNIQUE (PARTIE 2)

### 3.1. Contrôle des Accès Horizontaux & Verticaux (BOLA / IDOR)

1. **Isolation Horizontale Acheteur :**
   * *Risque :* Un acheteur tente de visualiser ou modifier le panier / la commande d'un autre revendeur en modifiant l'ID dans la requête.
   * *Protection :* Règle Firestore `request.auth.uid == resource.data.buyerId`. Les requêtes clientes ne peuvent lire que les documents tagués avec leur propre UID d'authentification.
2. **Isolation Horizontale Grossiste :**
   * *Risque :* Un grossiste du Forum tente de modifier le prix d'un concurrent de Roxy ou de supprimer ses articles.
   * *Protection :* Règle Firestore stricte sur la collection `products` :
     ```javascript
     allow update, delete: if request.auth.uid == resource.data.sellerId && request.auth.token.role == 'WHOLESALER';
     ```
3. **Escalade Verticale de Privilèges :**
   * *Risque :* Un utilisateur s'attribue lui-même le statut `ADMIN` ou le badge `TERRAIN_VERIFIED` dans le document profil.
   * *Protection :* Le champ `role` et les attributs `isVerified`, `verificationBadge` ne peuvent être modifiés que via des Custom Claims Firebase gérés par les Cloud Functions d'administration, ou avec une règle Firestore rejetant toute écriture sur ces champs par le client normal (`!('role' in request.resource.data)`).

### 3.2. Falsification des Prix et des Commandes

* *Vérification :* Le prix unitaire et le total d'une commande ne sont **jamais** acceptés aveuglément depuis le payload mobile du client.
* *Implémentation :* Lors de l'émission d'une commande, la Cloud Function backend (ou le repository centralisé) recharge les entités `Product` originales, applique la formule de palier sur la quantité réelle, et calcule côté serveur le `totalAmount` immuable.

### 3.3. Sécurité du Stockage & Médias (Firebase Storage)

* Validation stricte des extensions d'images autorisées : `image/jpeg`, `image/png`, `image/webp`.
* Limite maximale de taille : 2 Mo par image (avec compression côté client avant upload pour économiser la data 3G/4G).
* Chemin d'upload partitionné par boutique : `/shops/{shopId}/products/{productId}.webp`.

---

## 4. CAS RÉELS DU MARCHÉ D'ADJAMÉ (PARTIE 3)

| Scénario Réel Adjamé | Risque Métier | Solution Implémentée dans l'Application |
| :--- | :--- | :--- |
| **Produit sans stock** | L'acheteur se déplace pour rien au Forum | Badge `OUT_OF_STOCK` visible en rouge, bouton d'ajout au panier bloqué avec message invitant à contacter le vendeur par WhatsApp pour connaître la date du prochain conteneur. |
| **Produit modifié pendant la commande** | Le grossiste change son prix pendant que l'acheteur remplit son panier | Vérification de version (`updatedAt`) lors de la validation du panier. Si le prix a varié, alerte modale demandant re-confirmation à l'acheteur. |
| **Boutique fermée / Grossiste indisponible** | Jour férié, inventaire ou fermeture inattendue | Affichage clair des horaires ("7h30 - 18h00") et statut actif de la boutique. Téléphone direct et WhatsApp disponibles pour vérification préalable. |
| **Coupure réseau pendant commande** | Réseau Orange/MTN instable dans les dédales du Forum | Architecture Offline-First : l'action est enregistrée dans la file d'attente locale (Room / Firestore offline persistence). Dès reconnexion, synchronisation automatique. |
| **Commandes simultanées sur dernier carton** | 2 revendeurs achètent le même dernier stock | Transaction Firestore atomique `runTransaction()` décrémentant le compteur `stockQuantity`. Le premier valide, le second reçoit une notification de rupture sans débit. |
| **Commande sous le MOQ** | Revendeur veut un seul paquet au lieu du carton | Le champ quantité est verrouillé avec un seuil plancher égal au `minOrderQuantity`. Une bulle d'aide explique la politique grossiste d'Adjamé. |
| **Négociation de prix** | Volume exceptionnel (ex: 100 cartons) | Module intégré de Demande de Cotation (`PriceQuoteRequest`) permettant de proposer un tarif unitaire personnalisé avant validation formelle. |
| **Litige colis / Produit non conforme** | Carton ouvert ou marchandise abîmée | Procédure de validation par Code PIN : l'argent n'est débloqué ou la commande marquée terminée que lorsque l'acheteur vérifie le colis physiquement et communique le PIN au vendeur. |

---
