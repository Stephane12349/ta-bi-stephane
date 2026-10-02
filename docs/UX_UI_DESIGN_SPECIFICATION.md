# SPÉCIFICATION UX/UI COMPLÈTE & DESIGN SYSTEM B2B
## ADJAMÉ MARKET (CÔTE D'IVOIRE) — MOBILE-FIRST & WEB

---

## 1. OBJECTIF UX & VISION DU PRODUIT

L'expérience utilisateur est conçue pour résoudre le défi fondamental du commerce de gros informel en Côte d'Ivoire :
$$\text{« Je cherche un produit »} \longrightarrow \text{« J'ai trouvé un grossiste fiable et je peux le contacter / commander »}$$
en **moins de 4 interactions tactiles** et avec **zéro ambiguïté sur la disponibilité, les prix de gros et l'existence physique du magasin**.

### Principes Directeurs
1. **Clarté Radicale (Zéro friction cognitive) :** Vocabulaire adapté au commerce abidjanais (*Prix par carton*, *Minimum de commande*, *Arrivages*, *Magasin B-14 Face pharmacie Mirador*).
2. **Confiance Immuable ("Anti-Gbatage") :** Vérification physique sur le terrain, visibilité de l'adresse réelle et des badges certifiés, notations communautaires transparentes.
3. **Résilience Réseau & Faible Débit :** Poids plume (< 10 Mo), cache local persistant Room/IndexedDB, images WebP ultra-légères, mode dégradé hors-ligne.
4. **Continuité Marchande :** Deep-linking direct WhatsApp Business et appel téléphonique prioritaire.

---

## 2. DESIGN SYSTEM & DESIGN TOKENS

### 2.1. Palette Chromatique
| Token | Valeur Hex | Rôle & Signification Psychologique |
| :--- | :---: | :--- |
| `color-primary` | `#0E6251` | Vert Marché Émeraude : Confiance, prospérité financière, authenticité. |
| `color-primary-container` | `#E8F8F5` | Fond d'accentuation doux pour badges actifs et sélections. |
| `color-primary-on` | `#FFFFFF` | Texte sur fond vert principal. |
| `color-secondary` | `#D35400` | Orange Adjamé : Chaleur, énergie, dynamisme des arrivages et négociation. |
| `color-secondary-container`| `#FBEEE6` | Arrière-plan pour remises volume et alertes promos. |
| `color-secondary-on` | `#FFFFFF` | Texte sur fond orange. |
| `color-whatsapp` | `#25D366` | Vert officiel WhatsApp : Déclencheur roi de conversion. |
| `color-surface` | `#FFFFFF` | Fond de page et cartes principales. |
| `color-surface-variant` | `#F8F9FA` | Fond de contraste pour listes et séparateurs doux. |
| `color-border` | `#E2E8F0` | Bordure fine (1dp) délimitant les composants sans alourdir le rendu GPU. |
| `color-text-primary` | `#1A252C` | Lisibilité maximale même sous la forte luminosité solaire d'Abidjan. |
| `color-text-secondary` | `#566573` | Sous-titres, conditionnements, délais de réponse. |
| `color-text-tertiary` | `#85929E` | Mentions secondaires, mentions d'aide, dates. |
| `color-status-success` | `#27AE60` | Vert Validation : Boutique vérifiée terrain, paiement reçu, en stock. |
| `color-status-warning` | `#F39C12` | Orange Alerte : Stock limité (< 5 cartons), délai de réponse modéré. |
| `color-status-error` | `#C0392B` | Rouge Rupture / Litige : Rupture de stock, problème de commande. |
| `color-badge-gold` | `#D4AF37` | Or Prestige : Grossiste certifié importateur de premier rang. |

### 2.2. Typographie
- **Police Principale :** *Plus Jakarta Sans* ou *Inter* (Google Fonts téléchargées en local pour zéro latence réseau).
- **Échelle Typographique :**
  - `display-lg` : 32sp, Line-height 40sp, Bold (700) — Splash, totaux panier, chiffres clés dashboard.
  - `headline-md` : 22sp, Line-height 28sp, SemiBold (600) — Noms de boutiques, titres d'écrans.
  - `title-md` : 18sp, Line-height 24sp, SemiBold (600) — Noms de produits, en-têtes de modales.
  - `body-lg` : 16sp, Line-height 22sp, Regular (400) — Descriptions, saisies de formulaires.
  - `body-md` : 14sp, Line-height 20sp, Medium (500) — Paliers de prix, conditionnements (cartons).
  - `label-sm` : 12sp, Line-height 16sp, Bold (700) — Badges "Vérifié", MOQ, tags de catégories.

### 2.3. Espacements & Grille (8dp Grid)
- `space-2` (2dp) / `space-4` (4dp) : Séparation interne icône/texte.
- `space-8` (8dp) : Marges internes de chips, padding d'éléments groupés.
- `space-12` (12dp) : Padding horizontal des champs de saisie.
- `space-16` (16dp) : Marges d'écran standards (gouttière mobile), espacement entre cartes.
- `space-24` (24dp) : Espacement entre sections majeures d'un écran.
- `space-32` (32dp) : Marge haute et basse des boutons d'action d'en-tête.

### 2.4. Rayons de Courbure (Border Radius)
- `radius-sm` : 8dp (Chips, tags de filtres, boutons d'action rapide).
- `radius-md` : 12dp (Cartes produits, champs de formulaires, dialogs).
- `radius-lg` : 16dp (Cartes de boutiques en vedette, bottom sheets modales).
- `radius-pill` : 999dp (Pills de statut, boutons d'appel/WhatsApp flottants).

### 2.5. Ombres & Élévation
Afin de minimiser la consommation de batterie et les ralentissements graphiques sur smartphones d'entrée de gamme (Android Go, 2 Go RAM), les ombres floues lourdes sont proscrites.
- `elevation-0` : Plat avec bordure fine 1dp `#E2E8F0`.
- `elevation-card` : `tonalElevation = 1.dp` + ombre douce `0px 2px 4px rgba(0, 0, 0, 0.04)`.
- `elevation-overlay` : Bottom sheets et modales : `0px -4px 16px rgba(0, 0, 0, 0.12)`.

### 2.6. Bibliothèque de Composants Réutilisables
1. **Bouton Primaire M3 :** Hauteur 52dp, zone tactile $\ge 48\text{dp}$, fond `#0E6251`, texte blanc 16sp gras, ripple feedback.
2. **Bouton Négociation WhatsApp :** Fond `#25D366`, icône WhatsApp vectorielle blanche, texte blanc 15sp semi-bold, largeur 100% sur mobile.
3. **Badge "Boutique Vérifiée Terrain" :** Fond `#E8F8F5`, bordure 1dp `#27AE60`, icône bouclier doré, texte `#0E6251` 12sp gras.
4. **Carte Produit B2B :** Format grille 2 colonnes ou liste étendue, affichage du packaging en pilule grise, prix unitaire de base en gros caractères, MOQ souligné.
5. **Tableau des Paliers de Gros :** Grille claire 2 colonnes (Volume $\leftrightarrow$ Prix/carton) avec surbrillance dynamique du palier atteint selon la quantité saisie.
6. **États Systèmes :**
   - *Loading :* Skeleton Shimmer gris clair pulsant (faible empreinte mémoire).
   - *Empty :* Illustration sobre + texte encourageant en français clair + bouton d'action alternative.
   - *Error :* Alerte explicite avec diagnostic simplifié (*"Réseau indisponible - Affichage du cache local"*).
   - *Success :* Checkmark animé et code de transaction géant à 4 chiffres.

---

## 3. SITEMAP & ARCHITECTURE DE NAVIGATION

```
                                          [ APPLICATION ADJAMÉ MARKET ]
                                                         |
         +-----------------------------------------------+-----------------------------------------------+
         |                                               |                                               |
  [ AUTH & ONBOARDING ]                          [ ESPACE ACHETEUR ]                            [ ESPACE GROSSISTE ]
  - 01. Splash                                   - 07. Accueil (Home B2B)                       - G01. Dashboard Synthèse
  - 02. Onboarding (3 slides)                    - 08. Recherche Textuelle & Vocale             - G02. Profil & Vitrine Boutique
  - 03. Connexion Téléphone                      - 09. Résultats & Vues                         - G03. Certification Terrain KYC
  - 04. Inscription Acheteur                     - 10. Filtres Avancés (Bottom Sheet)           - G04. Catalogue & Inventaire
  - 05. Vérification OTP                         - 11. Catégories & Filières                    - G05. Ajout Produit Express (<60s)
  - 06. Autorisation Localisation                - 12. Boutique Grossiste (Vitrine)             - G06. Modification Prix & Paliers
                                                 - 13. Catalogue de la Boutique                 - G07. Gestion des Stocks
                                                 - 14. Fiche Produit (MOQ & Paliers)            - G08. Commandes Reçues
                                                 - 15. Panier / Liste de Gros                   - G09. Devis & Cotations
                                                 - 16. Demande de Cotation In-App               - G10. Messagerie Marchande
                                                 - 17. Récapitulatif Commande                   - G11. Carnet Clients B2B
                                                 - 18. Confirmation & Reçu Code PIN             - G12. Statistiques Ventes
                                                 - 19. Historique Commandes                     - G13. Paramètres & Commis
                                                 - 20. Détail Commande                          - G14. Abonnement & Boosts
                                                 - 21. Messagerie Interne
                                                 - 22. Notifications & Alertes Arrivages        [ BACK-OFFICE ADMIN ]
                                                 - 23. Favoris & Carnet Fournisseurs            - A01. Dashboard National
                                                 - 24. Profil Utilisateur                       - A02. Validation Terrain KYC
                                                 - 25. Paramètres & Économiseur Data            - A03. Modération Annonces
                                                 - 26. Signalement / Litige                     - A04. Gestion Catégories
                                                 - 27. Aide & Support WhatsApp Direct           - A05. Gestion Litiges
                                                                                                - A06. Baromètre Prix Hebdo
```

---

## 4. SPÉCIFICATION DÉTAILLÉE DES 27 ÉCRANS ACHETEUR

Pour chaque écran, l'analyse respecte rigoureusement les 9 dimensions demandées.

---

### ÉCRAN 01 : SPLASH SCREEN
1. **Objectif :** Lancer l'application en moins de 800ms, vérifier l'état d'authentification et initialiser le cache local Room/IndexedDB.
2. **Composants :** Logo vectoriel centré "Adjamé Market", baseline *"Le carrefour des grossistes de Côte d'Ivoire"*, indicateur de chargement linéaire discret en bas d'écran.
3. **Informations affichées :** Logo de marque, version de l'application (ex: v1.0.0-ci).
4. **Actions possibles :** Aucune (transition automatique).
5. **États :** Initialisation $\rightarrow$ Prêt.
6. **Erreurs :** Erreur de chargement du cache local $\rightarrow$ Bascule automatique en mode secours sans bloquer l'écran.
7. **Comportement responsive :** Centrage parfait quel que soit le ratio d'aspect (16:9, 19.5:9, 21:9).
8. **Comportement mobile :** Affichage plein écran sans interruption (`enableEdgeToEdge`).
9. **Comportement desktop :** Logo centré dans une fenêtre d'accueil sobre avec fond dégradé subtil vert marché.

---

### ÉCRAN 02 : ONBOARDING (CARROUSEL 3 ÉTAPES)
1. **Objectif :** Présenter les 3 piliers de valeur B2B et rassurer l'utilisateur en moins de 15 secondes.
2. **Composants :** Carrousel horizontal à swipe, 3 illustrations épurées, indicateur de pagination à 3 points, bouton primaire *"Suivant"* / *"Commencer"*, bouton texte *"Passer"*.
3. **Informations affichées :**
   - Slide 1 : *"Achetez au prix de gros d'Adjamé sans quitter votre boutique."*
   - Slide 2 : *"Zéro Gbatage : 100% des grossistes vérifiés sur le terrain par nos équipes."*
   - Slide 3 : *"Négociez sur WhatsApp et faites-vous livrer en gare ou en boutique."*
4. **Actions possibles :** Glisser vers la gauche/droite, cliquer sur *"Suivant"*, cliquer sur *"Passer"*, cliquer sur *"Commencer"*.
5. **États :** Slide 1, Slide 2, Slide 3 actif.
6. **Erreurs :** Aucune.
7. **Comportement responsive :** Conteneur centré avec largeur max de 480dp sur grands écrans.
8. **Comportement mobile :** Interaction gestuelle fluide au doigt (swipe naturel).
9. **Comportement desktop :** Flèches de navigation gauche/droite et navigation au clavier.

---

### ÉCRAN 03 : CONNEXION
1. **Objectif :** Identifier le commerçant avec son numéro de téléphone sans mot de passe à mémoriser.
2. **Composants :** Top bar avec bouton retour, champ numéro avec indicatif fixe `+225` et drapeau ivoirien, bouton primaire *"Recevoir mon code par SMS"*, bouton secondaire *"Recevoir par WhatsApp"*, lien d'orientation *"Vous êtes grossiste ? Accéder à l'espace vendeur"*.
3. **Informations affichées :** Titre *"Bon retour parmi nous"*, sous-titre explicatif sur l'envoi d'un code OTP gratuit.
4. **Actions possibles :** Saisir son numéro (10 chiffres), choisir SMS ou WhatsApp, changer d'espace utilisateur.
5. **États :** Champ vide, Champ en cours de saisie, Bouton désactivé (numéro incomplet), Bouton actif (10 chiffres valides).
6. **Erreurs :** Numéro invalide (ex: format non ivoirien), limite d'envois SMS atteinte $\rightarrow$ Message d'aide avec proposition de bascule sur WhatsApp.
7. **Comportement responsive :** Centré sur écran large avec carte délimitée.
8. **Comportement mobile :** Pavé numérique spécialisé (`KeyboardType.Phone`) ouvert automatiquement.
9. **Comportement desktop :** Focus automatique sur le champ avec validation à la touche Entrée.

---

### ÉCRAN 04 : INSCRIPTION ACHETEUR
1. **Objectif :** Créer un profil acheteur qualifié en moins de 30 secondes.
2. **Composants :** Formulaire à 3 champs : Nom complet / Nom de commerce, Sélecteur de type d'activité (Chips : *Boutique physique, Vendeur WhatsApp/TikTok, Revendeur ambulant, Demi-grossiste*), Menu déroulant des communes/villes de Côte d'Ivoire (Abidjan : Yopougon, Cocody, Adjamé, Abobo, etc. ; Intérieur : Bouaké, San Pedro, Yamoussoukro, etc.), bouton *"Continuer"*.
3. **Informations affichées :** Titre *"Rejoignez le réseau des commerçants"*, mention *"Vos informations restent confidentielles"*.
4. **Actions possibles :** Remplir les champs, sélectionner son profil marchand, valider.
5. **États :** Formulaire vierge, partiellement rempli, valide.
6. **Erreurs :** Nom trop court, commune non sélectionnée $\rightarrow$ Indication rouge sous le champ concerné.
7. **Comportement responsive :** Formulaire sur une colonne centrée (max 520dp).
8. **Comportement mobile :** Scroll vertical doux pour maintenir le bouton d'action visible au-dessus du clavier virtuel.
9. **Comportement desktop :** Mise en page aérée avec icônes illustrant chaque type de commerce.

---

### ÉCRAN 05 : VÉRIFICATION TÉLÉPHONE (OTP)
1. **Objectif :** Valider l'authenticité du numéro de téléphone via code à usage unique.
2. **Composants :** 6 cases de saisie de chiffres grand format (taille 50x56dp), compte à rebours de renvoi (45s), bouton texte *"Renvoyer le code"*, bouton d'assistance *"Je n'ai pas reçu le code"*.
3. **Informations affichées :** Numéro masqué destinataire (ex: `+225 07 •• •• 10 11`), minuteur dégressif.
4. **Actions possibles :** Saisir les 6 chiffres, coller le code depuis le presse-papier, demander un renvoi par SMS ou WhatsApp.
5. **États :** Saisie en cours, validation automatique dès le 6ème chiffre entré, état d'erreur si code erroné (secousse haptique et bordure rouge).
6. **Erreurs :** Code périmé ou incorrect $\rightarrow$ Message clair en rouge *"Code erroné, veuillez réessayer"*.
7. **Comportement responsive :** Centré horizontalement et verticalement.
8. **Comportement mobile :** Écouteur automatique des SMS entrants (`SmsRetriever`) pour auto-remplissage sans effort.
9. **Comportement desktop :** Support du copier-coller global (Cmd+V / Ctrl+V).

---

### ÉCRAN 06 : AUTORISATION LOCALISATION
1. **Objectif :** Obtenir la géolocalisation pour calculer les distances avec les magasins d'Adjamé et proposer les gares de fret adaptées.
2. **Composants :** Illustration vectorielle d'Adjamé et d'Abidjan, texte d'explication pédagogique, bouton primaire *"Activer la localisation"*, bouton secondaire *"Choisir manuellement ma commune"*.
3. **Informations affichées :** *"Trouvez les grossistes les plus proches de chez vous et estimez les coûts de transport en toute transparence."*
4. **Actions possibles :** Accepter la permission système Android/Web, ou ouvrir la liste manuelle des communes.
5. **États :** Demande en attente, permission accordée, permission refusée.
6. **Erreurs :** Refus persistant $\rightarrow$ Sélection manuelle de secours sans blocage de l'utilisateur.
7. **Comportement responsive :** Boîte de dialogue centrée sur desktop / Bottom card sur mobile.
8. **Comportement mobile :** Dialogue natif de permission `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION`.
9. **Comportement desktop :** Demande de géolocalisation du navigateur avec alternative de choix par liste déroulante.

---

### ÉCRAN 07 : ACCUEIL (HOME B2B)
1. **Objectif :** Permettre l'orientation et la découverte immédiate des grossistes et des arrivages.
2. **Composants :**
   - Header fixe : Sélecteur de commune actuelle (*"Livraison à : Yopougon"*), icône cloche de notifications avec pastille rouge, icône carnet favoris.
   - Barre de recherche proéminente avec micro pour recherche vocale et bouton d'accès rapide aux filtres.
   - Bannière "Arrivages Conteneurs du Jour" (carrousel dynamique des grossistes venant de décharger).
   - Rangée de tuiles de catégories rondes/carrées (Cosmétiques, High-Tech, Pagnes, Bazar, Vivrier).
   - Section *"Grossistes Recommandés"* : Cartes horizontales avec logo, note, secteur (ex: Forum B-14) et bouton WhatsApp direct.
   - Section *"Produits Populaires au Carton"* : Grille 2 colonnes avec prix dégressif et badge de stock.
   - Bannière discrète *"Mode Économie de Données"* pour activer le chargement ultra-léger.
3. **Informations affichées :** Noms des boutiques vérifiées, photos d'articles, prix de gros par carton, MOQ, secteur d'Adjamé.
4. **Actions possibles :** Cliquer sur la recherche, filtrer par catégorie, consulter un produit, contacter directement un grossiste, basculer en mode économie data.
5. **États :** Normal, Chargement Shimmer, Mode dégradé hors-ligne (bannière jaune *"Données issues du cache local"*).
6. **Erreurs :** Panne réseau $\rightarrow$ Affichage transparent des données en cache sans pop-up intempestif.
7. **Comportement responsive :** Grille 2 colonnes sur mobile $\rightarrow$ 3 colonnes sur tablette $\rightarrow$ 4 colonnes sur desktop.
8. **Comportement mobile :** Pull-to-refresh pour actualiser les arrivages, barre de navigation basse visible.
9. **Comportement desktop :** En-tête élargi avec barre de recherche pleine largeur et filtres rapides toujours visibles.

---

### ÉCRAN 08 : RECHERCHE DÉDIÉE (TEXTE & VOCALE)
1. **Objectif :** Permettre de trouver une référence en moins de 3 secondes.
2. **Composants :** Champ de recherche actif avec focus automatique, bouton de dictée vocale au micro, bouton d'effacement rapide "X", liste des 5 dernières recherches avec icône horloge et bouton suppression, tags des termes les plus recherchés du moment (*"Savon Kanza"*, *"Écouteurs TWS"*, *"Pagne Woodin"*), recherche par sous-marché (*Forum, Black Market, Gouro*).
3. **Informations affichées :** Historique personnel, suggestions populaires à Abidjan.
4. **Actions possibles :** Taper du texte, dicter le nom en français/nouchi, cliquer sur une suggestion, effacer l'historique.
5. **États :** Champ vide avec suggestions, champ rempli avec auto-complétion instantanée.
6. **Erreurs :** Micro indisponible $\rightarrow$ Message invitant à taper au clavier.
7. **Comportement responsive :** Largeur max 600dp sur desktop.
8. **Comportement mobile :** Clavier virtuel ouvert automatiquement, bouton "Rechercher" sur le clavier.
9. **Comportement desktop :** Navigation dans la liste des suggestions via les touches fléchées haut/bas.

---

### ÉCRAN 09 : RÉSULTATS DE RECHERCHE
1. **Objectif :** Présenter les résultats avec une lisibilité commerciale optimale pour comparer les offres.
2. **Composants :** Barre supérieure avec rappel de la requête et nombre de résultats (*"42 résultats pour Savon"*), bouton de bascule d'affichage (Grille 2 colonnes / Liste détaillée), barre de filtres horizontaux défilante (*Prix croissant*, *En stock*, *Forum Adjamé*, *Vérifié*), bouton flottant "Filtres (3)".
3. **Informations affichées par carte :** Photo de l'article, nom du produit, conditionnement (ex: *Carton de 48 pcs*), MOQ, prix de gros unitaire, nom de la boutique avec badge vérifié et secteur.
4. **Actions possibles :** Cliquer sur un produit pour voir la fiche, cliquer sur le logo de la boutique pour voir sa vitrine, ouvrir les filtres avancés, trier par prix.
5. **États :** Résultats disponibles, aucun résultat (Empty State avec suggestions de termes proches), chargement avec skeleton.
6. **Erreurs :** Erreur réseau $\rightarrow$ Résultats tirés de la base de données Room locale.
7. **Comportement responsive :** Mobile : 2 colonnes ; Tablette : 3 colonnes ; Desktop : 4 colonnes avec panneau latéral de filtres.
8. **Comportement mobile :** Scroll infini avec pagination par blocs de 20 articles.
9. **Comportement desktop :** Tri par liste déroulante (*Pertinence, Prix croissant, Quantité minimale*).

---

### ÉCRAN 10 : FILTRES AVANCÉS (MODAL BOTTOM SHEET)
1. **Objectif :** Permettre un ciblage B2B précis sans noyer l'utilisateur.
2. **Composants :**
   - Header avec titre *"Filtres"* et bouton *"Réinitialiser"*.
   - Sélecteur de secteur d'Adjamé (Chips à sélection multiple : *Forum, Black Market, Marché Gouro, Roxy, Dallas, Mirador*).
   - Conditionnement (Chips : *Au carton, À la douzaine, Au sac, Au ballot, Au paquet*).
   - Curseur de Quantité Minimale (MOQ) : De 1 à 50 unités/cartons.
   - Fourchette de prix en FCFA : Saisie manuelle Min / Max ou slider à double curseur.
   - Interrupteurs à bascule (Switch) : *"Grossiste vérifié terrain uniquement"*, *"Disponible immédiatement en boutique"*, *"Possibilité d'expédition en gare"*.
   - Bouton fixe en bas : *"Appliquer les filtres (X articles trouvés)"*.
3. **Informations affichées :** Valeurs sélectionnées, compteur dynamique d'articles correspondants en temps réel.
4. **Actions possibles :** Cocher/décocher des critères, ajuster les curseurs, réinitialiser, appliquer.
5. **États :** Filtres par défaut, filtres modifiés.
6. **Erreurs :** Prix minimum supérieur au prix maximum $\rightarrow$ Correction automatique des bornes.
7. **Comportement responsive :** Bottom sheet sur mobile / Volet latéral ou modale centrée sur desktop.
8. **Comportement mobile :** Glissement vers le bas pour fermer la modale sans appliquer.
9. **Comportement desktop :** Affichage permanent en colonne gauche de l'écran des résultats.

---

### ÉCRAN 11 : CATÉGORIES & FILIÈRES DU MARCHÉ
1. **Objectif :** Permettre d'explorer l'ensemble des filières marchandes d'Adjamé de manière structurée.
2. **Composants :** Grille des catégories principales avec icônes grand format, volet accordéon pour déplier les sous-catégories (ex: *High-Tech $\rightarrow$ Chargeurs rapides, Écouteurs sans-fil, Câbles USB, Enceintes, Pièces détachées*), badge du nombre d'articles disponibles par catégorie.
3. **Informations affichées :** Nom de la filière, description courte, volume de produits répertoriés.
4. **Actions possibles :** Cliquer sur une catégorie pour voir ses produits, déplier une sous-famille.
5. **États :** Normal, Déplié, Chargement.
6. **Erreurs :** Aucune.
7. **Comportement responsive :** Grille 2 colonnes sur smartphone, 4 colonnes sur grand écran.
8. **Comportement mobile :** Liste fluide avec séparateurs visuels légers.
9. **Comportement desktop :** Arborescence arborescente à deux niveaux visible en un coup d'œil.

---

### ÉCRAN 12 : BOUTIQUE GROSSISTE (VITRINE MAGASIN)
1. **Objectif :** Présenter le profil complet du grossiste, prouver son existence physique et instaurer une confiance absolue.
2. **Composants :**
   - Photo de devanture réelle du magasin + logo de la boutique.
   - Nom commercial de l'établissement + Badge doré *"Vérifié sur le Terrain"*.
   - Repères géographiques ivoiriens détaillés (*"Adjamé Forum, Niveau 1, Couloir 4, Magasin 112, Face pharmacie"*).
   - Indicateur de statut en direct : *"Ouvert actuellement • Ferme à 18h00"*.
   - Indicateurs de confiance : Note moyenne (ex: 4.9/5 sur 184 avis), ancienneté sur le marché (ex: *"Présent depuis 8 ans"*), délai moyen de réponse (*"Répond en moins de 10 min"*).
   - Barre d'actions rapides fixes : Bouton vert *"Négocier sur WhatsApp"*, Bouton *"Appel vocal direct"*, Bouton *"Enregistrer dans mes contacts favoris"*.
   - Onglets de contenu : *Catalogue complet*, *Arrivages récents*, *Avis des détaillants*, *Conditions de vente & livraison*.
3. **Informations affichées :** Coordonnées, photos de stock physique, spécialités, conditions de transport (gare UTB, CTE, livraison tricycle Abidjan).
4. **Actions possibles :** Lancer un appel, ouvrir WhatsApp, parcourir le catalogue, lire les avis, copier l'adresse physique, partager la fiche.
5. **États :** Boutique ouverte, boutique fermée, en cours de vérification terrain.
6. **Erreurs :** Numéro de téléphone indisponible $\rightarrow$ Message invitant à passer par WhatsApp ou la messagerie interne.
7. **Comportement responsive :** En-tête hero adaptatif, onglets fixés lors du défilement.
8. **Comportement mobile :** Barre d'action sticky en bas d'écran (Call / WhatsApp).
9. **Comportement desktop :** Présentation en deux volets (Informations boutique à gauche, catalogue à droite).

---

### ÉCRAN 13 : CATALOGUE DE LA BOUTIQUE
1. **Objectif :** Explorer l'intégralité des références d'un unique grossiste avec ses conditions de gros.
2. **Composants :** Barre de recherche interne au magasin, filtre par sous-catégorie interne, liste/grille des produits du vendeur avec packaging et MOQ.
3. **Informations affichées :** Prix de gros par carton, disponibilité du stock, référence article.
4. **Actions possibles :** Rechercher un produit chez ce vendeur, ajouter au panier de gros, contacter le vendeur à propos d'un article spécifique.
5. **États :** Catalogue complet, résultat de recherche interne vide.
6. **Erreurs :** Produits indisponibles $\rightarrow$ Badge grisé *"Rupture temporaire"*.
7. **Comportement responsive :** S'ajuste dynamiquement selon la largeur d'écran.
8. **Comportement mobile :** Défilement vertical rapide avec images compressées.
9. **Comportement desktop :** Tableau de commande rapide avec saisie des quantités en série.

---

### ÉCRAN 14 : FICHE PRODUIT B2B (DÉCLENCHEUR CENTRAL)
1. **Objectif :** Afficher tous les détails techniques et tarifaires d'un produit pour déclencher la prise de contact ou la commande.
2. **Composants :**
   - Galerie photos HD zoomables avec indicateur de taille de fichier pour économie de data.
   - Titre exact du produit + conditionnement mis en avant (*"Carton de 48 pièces de 150g"*).
   - **Tableau dynamique des Paliers Dégressifs :**
     - 1 à 4 cartons : 24 000 FCFA / carton (500 F/pièce)
     - 5 à 19 cartons : 21 600 FCFA / carton (450 F/pièce) — *Remise 10%*
     - 20 cartons et + : 19 200 FCFA / carton (400 F/pièce) — *Remise 20%*
   - Badge MOQ obligatoire : *"Commande minimum : 1 carton"*.
   - Indicateur de disponibilité : *"En stock au Forum d'Adjamé (actualisé ce jour)"*.
   - Encadré vendeur cliquable avec badge vérifié et note.
   - Sélecteur de quantité dynamique (+ / - ou saisie manuelle) calculant en direct le total en FCFA et surlignant le palier obtenu.
   - Section description détaillée et spécifications (origine, composition, date de péremption si alimentaire/cosmétique).
   - Barre fixe inférieure (Sticky Footer) :
     - Bouton secondaire : *"Demande de Devis"*
     - Bouton principal vert : *"Négocier sur WhatsApp"* (génère un message structuré avec photo et quantité choisie).
3. **Informations affichées :** Prix par carton, prix ramené à la pièce pour calcul de marge revendeur, localisation exacte du stock.
4. **Actions possibles :** Ajuster la quantité, ouvrir WhatsApp avec message prérempli, envoyer une cotation in-app, voir le profil du grossiste, partager l'offre sur les réseaux sociaux.
5. **États :** Quantité valide $\ge \text{MOQ}$, Quantité inférieure au MOQ (bouton bloqué avec alerte *"Minimum 1 carton requis"*), rupture de stock.
6. **Erreurs :** Saisie d'une quantité invalide (texte ou zéro) $\rightarrow$ Rétablissement automatique au MOQ minimal.
7. **Comportement responsive :** Mobile : disposition verticale fluide ; Desktop : double panneau (Photos à gauche, prix/paliers/actions à droite).
8. **Comportement mobile :** Barre sticky permanente au bas de l'écran avec bouton WhatsApp accessible du pouce.
9. **Comportement desktop :** Zoom photo au survol de la souris, tableau de calcul des marges intégré.

---

### ÉCRAN 15 : PANIER / LISTE DE GROS
1. **Objectif :** Regrouper les articles d'un ou plusieurs grossistes avant demande de cotation groupée ou commande ferme.
2. **Composants :** Articles regroupés par boutique vendeuse, ajusteur de quantité au carton, alerte automatique si le total pour une boutique ne respecte pas son minimum de commande, calcul du sous-total par grossiste et du total général en FCFA, bouton *"Procéder à la commande / devis"*.
3. **Informations affichées :** Nom du grossiste, nom de chaque produit, conditionnement, prix unitaire par carton, total partiel.
4. **Actions possibles :** Modifier les quantités, supprimer un article, séparer les commandes par grossiste, vider le panier.
5. **États :** Panier non vide, panier vide (illustration et bouton *"Explorer les grossistes"*), panier avec avertissement MOQ non atteint.
6. **Erreurs :** Quantité saisie inférieure au minimum $\rightarrow$ Message d'erreur bloquant la commande pour cette boutique spécifique.
7. **Comportement responsive :** Liste pleine largeur sur mobile $\rightarrow$ Vue 2 colonnes sur desktop (Articles à gauche, résumé financier à droite).
8. **Comportement mobile :** Swipe-to-delete pour supprimer un produit rapidement.
9. **Comportement desktop :** Validation globale ou par boutique indépendante.

---

### ÉCRAN 16 : FORMULAIRE DE DEMANDE DE COTATION (DEVIS)
1. **Objectif :** Soumettre une proposition formelle pour un volume important ou négocier un tarif sur-mesure.
2. **Composants :** Récapitulatif des articles sélectionnés, champ de proposition de prix unitaire (optionnel), sélecteur de mode de livraison souhaité (Retrait boutique Adjamé, Livraison express moto/tricycle dans Abidjan, Dépôt en gare pour l'intérieur), champ texte ou note vocale pour les consignes particulières, bouton *"Envoyer ma demande de cotation"*.
3. **Informations affichées :** Coordonnées de l'acheteur, délai estimé de réponse du grossiste (*"Réponse sous 30 min"*).
4. **Actions possibles :** Saisir sa proposition, enregistrer une note vocale explicative, valider l'envoi.
5. **États :** Formulaire en cours de rédaction, envoi en cours, devis soumis.
6. **Erreurs :** Échec de transmission réseau $\rightarrow$ Sauvegarde automatique en brouillon local avec réexpédition automatique dès reconnexion.
7. **Comportement responsive :** Formulaire centré (max 540dp).
8. **Comportement mobile :** Enregistreur vocal simplifié (bouton micro à maintenir).
9. **Comportement desktop :** Saisie assistée avec raccourcis clavier.

---

### ÉCRAN 17 : RÉCAPITULATIF DE COMMANDE / RÉSERVATION
1. **Objectif :** Confirmer les détails logistiques et financiers avant mise de côté de la marchandise au magasin d'Adjamé.
2. **Composants :** Détails de la boutique vendeuse et de son adresse physique, liste des cartons commandés, mode de réception choisi (Click & Collect en magasin ou Expédition en gare), choix du mode de règlement (Paiement cash/Mobile Money sur place au magasin, ou Acompte de réservation via Wave/Orange Money/MTN MoMo), bouton *"Confirmer la réservation du stock"*.
3. **Informations affichées :** Montant total des marchandises, frais de transport estimés (le cas échéant), récapitulatif fiscal/proforma.
4. **Actions possibles :** Modifier les coordonnées de réception, choisir le moyen de paiement, valider la réservation.
5. **États :** En attente de validation, traitement du paiement Mobile Money, succès.
6. **Erreurs :** Solde Mobile Money insuffisant $\rightarrow$ Message invitant à recharger son compte ou à opter pour le paiement au comptoir.
7. **Comportement responsive :** Vue synthétique sur une ou deux colonnes selon l'écran.
8. **Comportement mobile :** Lancement direct de l'application Wave ou Orange Money via Deep Link sécurisé.
9. **Comportement desktop :** Affichage d'un QR code Mobile Money à scanner avec son téléphone.

---

### ÉCRAN 18 : CONFIRMATION DE COMMANDE & REÇU SÉCURISÉ
1. **Objectif :** Rassurer l'acheteur et générer le bon d'enlèvement sécurisé pour le retrait physique au marché.
2. **Composants :**
   - Icône de validation verte animée.
   - Titre *"Réservation Confirmée !"*.
   - **Code de retrait sécurisé à 4 chiffres (OTP Transaction)** affiché en caractères géants (ex: **`7 4 8 2`**) + QR Code scannable par le grossiste.
   - Coordonnées exactes du magasin physique d'Adjamé avec bouton *"Ouvrir l'itinéraire"*.
   - Bouton d'action *"Partager le bon de commande sur WhatsApp"* (pour l'envoyer à son coursier ou au grossiste).
   - Bouton de retour *"Retour à l'accueil"*.
3. **Informations affichées :** Numéro de commande unique, date et heure limite de mise de côté du stock (ex: *"Marchandise réservée jusqu'à demain 16h"*), montant total.
4. **Actions possibles :** Copier le code PIN, télécharger le reçu en PDF/Image, partager sur WhatsApp, lancer la navigation vers la boutique.
5. **États :** Code actif non utilisé, code validé par le grossiste.
6. **Erreurs :** Aucune.
7. **Comportement responsive :** Reçu centré sous forme de ticket détachable.
8. **Comportement mobile :** Raccourci pour capture d'écran facile et sauvegarde dans la galerie photo.
9. **Comportement desktop :** Bouton d'impression directe du bon de livraison.

---

### ÉCRAN 19 : HISTORIQUE DES COMMANDES & COTATIONS
1. **Objectif :** Suivre les commandes en cours et consulter les achats passés.
2. **Composants :** 3 onglets supérieurs : *En cours*, *Terminées*, *Devis en attente*. Cartes de commande avec date, nom du grossiste, montant total, badge de statut avec code couleur (*Réservé, Préparé, Retiré, Annulé*).
3. **Informations affichées :** Date de commande, nombre d'articles/cartons, nom du grossiste, montant en FCFA.
4. **Actions possibles :** Filtrer par date, cliquer sur une carte pour voir les détails, recommander un panier identique en 1 clic.
5. **États :** Liste remplie, liste vide (Empty state avec bouton *"Découvrir les offres"*).
6. **Erreurs :** Erreur réseau $\rightarrow$ Chargement direct depuis la base Room locale.
7. **Comportement responsive :** Liste simple sur mobile $\rightarrow$ Tableau structuré sur grand écran.
8. **Comportement mobile :** Défilement fluide avec séparateurs temporels (*Cette semaine, Ce mois-ci*).
9. **Comportement desktop :** Export de l'historique sous format Excel/CSV pour la comptabilité du commerçant.

---

### ÉCRAN 20 : DÉTAIL D'UNE COMMANDE
1. **Objectif :** Offrir une traçabilité totale étape par étape d'une commande spécifique.
2. **Composants :**
   - Stepper d'avancement visuel (4 étapes : *Commande envoyée $\rightarrow$ Préparée en magasin $\rightarrow$ En cours de transport/Prête $\rightarrow$ Réceptionnée*).
   - Rappel du code PIN de retrait (si commande non encore retirée).
   - Liste détaillée des articles et cartons avec prix unitaires.
   - Fiche du grossiste avec bouton d'appel direct.
   - Bouton *"Signaler un problème sur cette commande"*.
   - Bouton *"Laisser un avis sur le grossiste"* (accessible une fois la commande terminée).
3. **Informations affichées :** Heure de chaque étape, identité du préparateur/commis de magasin, preuve de dépôt en gare (numéro de décharge colis).
4. **Actions possibles :** Contacter le grossiste, vérifier le code PIN, télécharger la facture, émettre un avis.
5. **États :** En préparation, prête pour retrait, terminée, litige ouvert.
6. **Erreurs :** Retard de préparation $\rightarrow$ Alerte avec bouton direct pour contacter le gérant.
7. **Comportement responsive :** Largeur adaptée avec mise en valeur du stepper.
8. **Comportement mobile :** Affichage optimisé pour lecture rapide en plein magasin.
9. **Comportement desktop :** Affichage côte à côte des articles et du journal d'événements.

---

### ÉCRAN 21 : MESSAGERIE IN-APP (TCHAT MARCHAND)
1. **Objectif :** Permettre de discuter et négocier sans quitter l'écosystème de l'application pour les commerçants souhaitant une trace écrite officielle.
2. **Composants :** Liste des discussions avec photo de profil de la boutique, badge de statut en ligne/hors ligne, aperçu du dernier message, heure du dernier échange. Dans la conversation : bulles de texte, envoi de photos de stock, envoi de notes vocales, partage de fiches produits.
3. **Informations affichées :** Nom du grossiste, temps moyen de réponse, historique des messages échangés.
4. **Actions possibles :** Écrire un message, enregistrer un audio, envoyer une photo de produit recherché, basculer sur WhatsApp si souhaité.
5. **États :** Connecté, message envoyé, message distribué, message lu.
6. **Erreurs :** Coupure réseau $\rightarrow$ Message mis en attente d'envoi avec icône d'horloge.
7. **Comportement responsive :** Liste des discussions et fil de conversation sur deux colonnes sur desktop.
8. **Comportement mobile :** Clavier adapté avec accès direct aux photos et au micro.
9. **Comportement desktop :** Raccourcis clavier (Entrée pour envoyer, Shift+Entrée pour saut de ligne).

---

### ÉCRAN 22 : CENTRE DE NOTIFICATIONS & ALERTES ARRIVAGES
1. **Objectif :** Informer l'acheteur des événements critiques et des arrivages conteneurs sans saturer son attention.
2. **Composants :** Liste chronologique des alertes avec icônes thématiques (Vert = Commande/Devis, Orange = Arrivage conteneur, Or = Baisse de prix sur favori), bouton *"Tout marquer comme lu"*, filtre par type de notification.
3. **Informations affichées :** Titre court (*"Arrivage conteneur Forum"*, *"Votre devis a été accepté"*), extrait du texte, heure de réception.
4. **Actions possibles :** Cliquer sur une alerte pour ouvrir l'écran concerné, supprimer une notification, gérer ses préférences d'alertes.
5. **États :** Notifications lues / non lues (fond légèrement teinté).
6. **Erreurs :** Aucune.
7. **Comportement responsive :** Centré sur écran large (max 600dp).
8. **Comportement mobile :** Glissement horizontal pour supprimer une alerte.
9. **Comportement desktop :** Menu déroulant depuis la cloche dans le header supérieur.

---

### ÉCRAN 23 : FAVORIS & CARNET DE FOURNISSEURS ENREGISTRÉS
1. **Objectif :** Permettre un accès instantané et 100% hors-ligne aux grossistes habituels et aux articles surveillés.
2. **Composants :** Deux onglets principaux :
   - *Onglet 1 : Mes Grossistes Enregistrés (Carnet d'Adresses)* : Cartes des grossistes avec coordonnées directes, repère géographique physique, et champ de **notes privées** de l'acheteur (ex: *"Demander le commis Salif pour avoir 5% de remise"*).
   - *Onglet 2 : Articles Sauvegardés* : Liste des produits mis en veille avec notification automatique si le prix baisse.
3. **Informations affichées :** Coordonnées téléphoniques/WhatsApp, note personnelle, état du stock actuel.
4. **Actions possibles :** Lancer un appel, ouvrir WhatsApp, modifier sa note personnelle, supprimer un favori, commander immédiatement.
5. **États :** Liste avec favoris, liste vide (Message invitant à enregistrer ses boutiques préférées).
6. **Erreurs :** Aucune (fonctionne intégralement hors-ligne via Room Database).
7. **Comportement responsive :** Grille 2 colonnes ou liste détaillée.
8. **Comportement mobile :** Accès direct aux boutons Call/WhatsApp sur chaque carte.
9. **Comportement desktop :** Tableau de bord du carnet d'adresses fournisseurs.

---

### ÉCRAN 24 : PROFIL ACHETEUR & GESTION DU COMPTE
1. **Objectif :** Gérer ses informations commerciales et basculer facilement vers l'espace grossiste si applicable.
2. **Composants :** Avatar/Photo du commerce, nom du gérant, numéro de téléphone vérifié, type d'activité commerciale, commune de livraison par défaut, bouton d'action *"Basculer en Mode Grossiste"*, liens vers les paramètres, l'aide et la déconnexion.
3. **Informations affichées :** Statut du compte (Revendeur Vérifié), nombre total de commandes passées, économies de temps estimées.
4. **Actions possibles :** Mettre à jour ses coordonnées, changer de commune, demander un statut grossiste, se déconnecter.
5. **États :** Mode consultation, mode modification des informations.
6. **Erreurs :** Numéro de téléphone modifié sans validation OTP $\rightarrow$ Déclenchement de la procédure de re-vérification.
7. **Comportement responsive :** Carte de profil centrée.
8. **Comportement mobile :** Liste verticale avec flèches chevron classiques.
9. **Comportement desktop :** Panneau latéral d'onglets de paramètres du compte.

---

### ÉCRAN 25 : PARAMÈTRES & ÉCONOMIE DE DONNÉES
1. **Objectif :** Permettre à l'utilisateur de configurer l'application pour réduire drastiquement sa consommation de forfait internet.
2. **Composants :**
   - Interrupteur principal *"Mode Économie de Données"* (désactive les images HD, charge des vignettes WebP compressées < 25 Ko).
   - Option *"Télécharger le catalogue de base pour utilisation hors-ligne"*.
   - Choix des canaux de notifications (WhatsApp / SMS / Notifications push).
   - Bouton *"Vider le cache local"* (avec indication de l'espace mémoire libéré en Mo).
   - Mentions légales et politique de confidentialité adaptée à la Côte d'Ivoire (ARTCI).
3. **Informations affichées :** Espace disque utilisé par le cache, état de la synchronisation locale.
4. **Actions possibles :** Activer/désactiver l'économie de data, vider le cache, personnaliser les alertes.
5. **États :** Mode standard (photos nettes) vs Mode économique (photos compressées).
6. **Erreurs :** Échec de purge du cache $\rightarrow$ Message invitant à redémarrer l'application.
7. **Comportement responsive :** Liste de réglages M3 sur une colonne.
8. **Comportement mobile :** Bascules tactiles larges pour réglage rapide du pouce.
9. **Comportement desktop :** Présentation dans une fenêtre modale de préférences.

---

### ÉCRAN 26 : SIGNALEMENT DE BOUTIQUE / LITIGE MARCHAND
1. **Objectif :** Assurer la salubrité de la marketplace en permettant de signaler immédiatement toute fraude ou non-conformité.
2. **Composants :** Sélecteur du motif de signalement (Chips : *Boutique introuvable à l'adresse indiquée, Marchandise non conforme, Tentative d'arnaque sur acompte, Prix en magasin différent de l'appli, Comportement irrespectueux*), champ de description libre des faits, téléversement de photos de preuve (reçu, photo de l'article), bouton d'envoi *"Transmettre à l'équipe de sécurité"*.
3. **Informations affichées :** Engagement d'Adjamé Market : *"Tout signalement est instruit sous 2 heures par notre bureau terrain d'Adjamé."*
4. **Actions possibles :** Remplir le motif, joindre des photos, soumettre le signalement.
5. **États :** Formulaire vierge, envoi en cours, signalement enregistré avec numéro de dossier.
6. **Erreurs :** Absence de motif sélectionné $\rightarrow$ Blocage avec mise en évidence du sélecteur.
7. **Comportement responsive :** Formulaire centré (max 500dp).
8. **Comportement mobile :** Prise de photo directe via l'appareil photo du smartphone.
9. **Comportement desktop :** Glisser-déposer des pièces justificatives.

---

### ÉCRAN 27 : CENTRE D'AIDE & SUPPORT LOCAL
1. **Objectif :** Offrir une assistance humaine et rassurante en direct d'Abidjan.
2. **Composants :**
   - Bouton vert géant *"Discuter avec notre équipe sur WhatsApp"* (ouvre la conversation avec le service client 7j/7).
   - Bouton d'appel téléphonique direct vers le numéro vert d'assistance.
   - Foire aux questions (FAQ) déroulante avec questions récurrentes (*"Comment être sûr qu'un grossiste existe ?", "Comment récupérer ma marchandise au Forum ?", "Que faire en cas de retard de livraison ?"*).
   - Adresse physique du bureau d'accueil Adjamé Market (pour les commerçants souhaitant une assistance en personne).
3. **Informations affichées :** Heures d'ouverture du support (Lundi au Samedi : 7h30 - 18h30), délais moyens de prise en charge (< 5 minutes).
4. **Actions possibles :** Lancer WhatsApp, appeler par téléphone, lire la FAQ.
5. **États :** Support ouvert / Support fermé (avec message invitant à laisser un mot sur WhatsApp).
6. **Erreurs :** Aucune.
7. **Comportement responsive :** Disposition claire sur une colonne.
8. **Comportement mobile :** Boutons d'appel et de WhatsApp prioritaires en haut d'écran.
9. **Comportement desktop :** Fenêtre de tchat d'assistance web intégrée.

---

## 5. SPÉCIFICATION DÉTAILLÉE DES 14 ÉCRANS GROSSISTE

L'Espace Grossiste adopte une identité visuelle distincte (teinte d'en-tête Bleu Nuit Pro `#1B263B` et Or `#D4AF37`) pour matérialiser l'outil de gestion commerciale professionnelle.

---

### ÉCRAN G01 : DASHBOARD GROSSISTE (SYNTHÈSE COMMERCIALE)
1. **Objectif :** Donner au patron ou à son commis la synthèse complète de la journée dès l'ouverture de l'application.
2. **Composants :**
   - En-tête avec nom de la boutique, photo de devanture et badge vérifié.
   - 4 cartes de métriques clés :
     - *Chiffre d'Affaires estimé du mois (en FCFA)*
     - *Demandes de cotation en attente (badge rouge d'urgence)*
     - *Nombre de cartons réservés aujourd'hui*
     - *Vues sur le catalogue ce jour*
   - Barre d'actions rapides : *"Publier un arrivage (<60s)"*, *"Voir mes commandes à préparer"*, *"Ajuster mes prix"*.
   - Liste des dernières demandes clients avec bouton d'acceptation en 1 clic.
3. **Informations affichées :** Montants en FCFA, alertes de commandes prioritaires, niveau de satisfaction des acheteurs (note moyenne).
4. **Actions possibles :** Consulter une demande, ouvrir une commande, ajouter un produit, contacter un acheteur.
5. **États :** Normal, alertes critiques de rupture ou commandes en retard.
6. **Erreurs :** Erreur de chargement des statistiques $\rightarrow$ Affichage des données de la veille avec mention de mise à jour.
7. **Comportement responsive :** Grille 2x2 de cartes sur mobile $\rightarrow$ Ligne de 4 cartes sur desktop.
8. **Comportement mobile :** Affichage optimisé pour manipulation rapide d'une seule main en boutique.
9. **Comportement desktop :** Dashboard complet avec graphiques d'évolution des ventes sur 30 jours.

---

### ÉCRAN G02 : PROFIL & VITRINE DE LA BOUTIQUE
1. **Objectif :** Configurer la présentation du magasin pour attirer et rassurer les revendeurs.
2. **Composants :** Formulaire de personnalisation : Nom de l'enseigne, photo de devanture (enseigne visible), photo de l'intérieur du dépôt, secteur précis (ex: *Adjamé Forum, Niveau 1, Magasin B-14*), repère visuel (ex: *Face pharmacie Mirador, couloir B*), horaires d'ouverture, conditions de livraison en gare.
3. **Informations affichées :** Aperçu en direct de ce que voient les acheteurs sur l'application.
4. **Actions possibles :** Modifier les textes, prendre de nouvelles photos de la boutique, mettre à jour les horaires.
5. **États :** Profil validé par l'agent terrain / Profil en attente d'audit.
6. **Erreurs :** Coordonnées incomplètes $\rightarrow$ Alerte invitant à préciser le numéro de magasin pour éviter les égarements des clients.
7. **Comportement responsive :** Aperçu dynamique côte à côte sur écran desktop.
8. **Comportement mobile :** Scroll vertical simple avec bouton de sauvegarde sticky.
9. **Comportement desktop :** Édition en mode WYSIWYG avec prévisualisation smartphone en temps réel.

---

### ÉCRAN G03 : CERTIFICATION TERRAIN & AUDIT KYC
1. **Objectif :** Permettre au grossiste de soumettre ses documents pour décrocher le badge officiel "Boutique Vérifiée Terrain".
2. **Composants :** Formulaire de téléversement : Photo de la CNI / Passeport du propriétaire, copie du bail commercial ou facture CIE/SODECI du magasin, bouton de demande de passage d'un ambassadeur terrain Adjamé Market, calendrier de choix du jour de passage.
3. **Informations affichées :** Avantages du badge : *"Les boutiques vérifiées reçoivent en moyenne 4 fois plus de commandes de l'intérieur du pays."*
4. **Actions possibles :** Téléverser les pièces, planifier la visite de l'agent, suivre l'état de l'audit.
5. **États :** Non soumis, Documents en cours d'examen, Visite terrain planifiée, Badge accordé.
6. **Erreurs :** Photo de document illisible $\rightarrow$ Notification invitant à reprendre la photo avec un meilleur éclairage.
7. **Comportement responsive :** Formulaire adapté centré.
8. **Comportement mobile :** Prise de photo directe avec recadrage automatique des documents d'identité.
9. **Comportement desktop :** Téléversement multiple par glisser-déposer de fichiers PDF/images.

---

### ÉCRAN G04 : CATALOGUE MARCHAND & INVENTAIRE
1. **Objectif :** Gérer l'ensemble de ses références en un coup d'œil avec bascule rapide de disponibilité.
2. **Composants :** Barre de recherche interne, filtres par catégorie, liste des produits avec vignette photo, prix au carton, MOQ, interrupteur instantané *"En stock / Épuisé"*, bouton *"Modifier"*, bouton flottant grand format `+` *"Ajouter un produit"*.
3. **Informations affichées :** Nombre total de références actives, alertes sur les stocks presque épuisés.
4. **Actions possibles :** Basculer la disponibilité d'un article en 1 tap, modifier le prix d'un carton, ajouter un nouvel article, supprimer une référence.
5. **États :** Liste d'articles, inventaire vide (incitation à publier le premier carton).
6. **Erreurs :** Aucune.
7. **Comportement responsive :** Liste sur smartphone $\rightarrow$ Tableau gestionnaire complet sur desktop.
8. **Comportement mobile :** Action rapide par glissement (Swipe gauche = Masquer, Swipe droite = Modifier prix).
9. **Comportement desktop :** Modification groupée des prix par pourcentage (ex: hausse conteneur +5%).

---

### ÉCRAN G05 : AJOUT RAPIDE D'UN PRODUIT (< 60 SECONDES)
1. **Objectif :** Permettre au grossiste de publier un arrivage de conteneur directement depuis son smartphone sans complexité.
2. **Composants :**
   - 3 blocs photos simples (1ère photo obligatoire via appareil photo ou galerie).
   - Champ Titre du produit (avec bouton de dictée vocale).
   - Sélecteur de catégorie en 1 tap (Chips horizontaux).
   - Conditionnement (Chips : *Carton de X pièces, Douzaine, Sac, Ballot*).
   - Minimum de Commande (MOQ) : Saisie numérique simple (ex: 2).
   - Prix unitaire de gros de base (en FCFA).
   - Paliers dégressifs optionnels (ex: *"À partir de 10 cartons : X FCFA"*).
   - Bouton d'action proéminent : *"Mettre en vente immédiatement"*.
3. **Informations affichées :** Prévisualisation du prix à la pièce calculé automatiquement pour vérifier la cohérence.
4. **Actions possibles :** Prendre les photos, dicter le titre à la voix, saisir les prix, publier.
5. **États :** Formulaire vierge, partiellement rempli, validation en cours.
6. **Erreurs :** Prix manquant ou photo absente $\rightarrow$ Signalement visuel immédiat sur le bloc concerné.
7. **Comportement responsive :** Formulaire centré (max 560dp).
8. **Comportement mobile :** Optimisé pour le clavier numérique avec passage automatique au champ suivant.
9. **Comportement desktop :** Téléversement en masse possible depuis des dossiers d'images.

---

### ÉCRAN G06 : MODIFICATION D'UN PRODUIT & PALIERS DE GROS
1. **Objectif :** Réajuster les conditions tarifaires d'un produit déjà en ligne selon la négociation ou les fluctuations de devises.
2. **Composants :** Mêmes champs que l'ajout avec valeurs préremplies, module de configuration des 3 paliers dégressifs (1 à 4 cartons, 5 à 19 cartons, 20+ cartons), bouton *"Enregistrer les modifications"*, bouton *"Supprimer l'article"*.
3. **Informations affichées :** Historique des modifications de prix antérieures.
4. **Actions possibles :** Ajuster les prix, modifier les paliers de quantité, sauvegarder, retirer du catalogue.
5. **États :** Modifications non enregistrées, enregistrement réussi.
6. **Erreurs :** Palier de quantité incohérent (ex: prix du palier 2 supérieur au palier 1) $\rightarrow$ Message d'erreur logique bloquant.
7. **Comportement responsive :** Formulaire adapté à l'écran.
8. **Comportement mobile :** Touches numériques directes pour changer les montants en FCFA.
9. **Comportement desktop :** Comparaison avec les prix moyens constatés sur la place d'Adjamé.

---

### ÉCRAN G07 : GESTION DES STOCKS & ALERTES RUPTURES
1. **Objectif :** Permettre un suivi du stock physique disponible pour ne jamais décevoir un revendeur qui se déplace en boutique.
2. **Composants :** Sélecteur rapide par produit du volume de cartons restants, seuil d'alerte personnalisable (ex: *"M'alerter lorsqu'il reste moins de 5 cartons"*), bouton de mise à jour d'un geste *"Tout le stock est conforme"*.
3. **Informations affichées :** Nombre de cartons déclarés, nombre de cartons réservés par des commandes en attente de retrait.
4. **Actions possibles :** Ajuster le solde de cartons, marquer un produit en réapprovisionnement, confirmer l'inventaire physique.
5. **États :** Stock suffisant (Vert), Stock faible (Orange), Rupture (Rouge).
6. **Erreurs :** Stock négatif impossible.
7. **Comportement responsive :** Vue liste sur mobile $\rightarrow$ Tableau d'entrepôt sur desktop.
8. **Comportement mobile :** Boutons d'incrémentation rapide (+1, +5, +10 cartons) pour gagner du temps.
9. **Comportement desktop :** Import d'inventaire Excel pour les grands magasins.

---

### ÉCRAN G08 : GESTION DES COMMANDES REÇUES
1. **Objectif :** Piloter la préparation des lots de marchandises et sécuriser la remise aux acheteurs ou aux transporteurs.
2. **Composants :** Cartes de commande triées par priorité temporelle, nom de l'acheteur, détail des cartons à préparer, mode de retrait (Enlèvement magasin ou Gare), bouton *"Marquer comme préparé / Prêt au retrait"*, champ de saisie du **Code PIN de retrait client à 4 chiffres** ou bouton de scan QR code pour valider la délivrance finale.
3. **Informations affichées :** Montant total de la commande, heure limite d'enlèvement convenue, numéro de téléphone de l'acheteur.
4. **Actions possibles :** Confirmer la préparation du lot, appeler l'acheteur en 1 clic, valider la remise en saisissant le code PIN du client, annuler la réservation.
5. **États :** À préparer (Jaune), Prête en magasin (Bleu), Délivrée / Retirée (Vert), Annulée (Gris).
6. **Erreurs :** Code PIN erroné saisi lors de la remise $\rightarrow$ Alerte rouge *"Code incorrect. Ne remettez pas la marchandise avant validation."*
7. **Comportement responsive :** Cartes verticales sur mobile $\rightarrow$ Vue Kanban par statut sur desktop (*À préparer | Prêt | Terminé*).
8. **Comportement mobile :** Bouton d'appel rapide intégré directement sur chaque carte de commande.
9. **Comportement desktop :** Impression d'étiquettes de colis à coller sur les cartons.

---

### ÉCRAN G09 : DEMANDES DE PRIX & COTATIONS B2B
1. **Objectif :** Traiter et négocier les demandes de gros volumes émanant des revendeurs régionaux.
2. **Composants :** Détail de la demande de cotation (articles, volume demandé, ville de destination), proposition de prix de l'acheteur, 3 boutons d'action rapide : *"Accepter le prix proposé"*, *"Faire une contre-proposition tarifaire"*, *"Basculer la négociation sur WhatsApp"*.
3. **Informations affichées :** Profil de l'acheteur (ancienneté, volume d'achats antérieurs, fiabilité).
4. **Actions possibles :** Accepter, refuser, envoyer un devis formel chiffré, démarrer une discussion.
5. **États :** Cotation en attente (alerte sonore à l'arrivée), cotation traitée, cotation expirée (après 24h).
6. **Erreurs :** Montant de contre-proposition erroné $\rightarrow$ Demande de confirmation avant envoi.
7. **Comportement responsive :** Adapté à tout format d'écran.
8. **Comportement mobile :** Réponse en 2 taps pour ne pas bloquer le commerçant pendant ses ventes physiques.
9. **Comportement desktop :** Historique complet des marges et négociation sur un panneau latéral.

---

### ÉCRAN G10 : MESSAGERIE COMMERCIALE
1. **Objectif :** Échanger avec les revendeurs et clarifier les modalités pratiques d'expédition vers les gares.
2. **Composants :** Liste des fils de discussion avec revendeurs actifs, champ de message, bouton note vocale, bouton de partage d'un bon de commande validé.
3. **Informations affichées :** Nom du revendeur, commune/ville de destination, commande associée à la discussion.
4. **Actions possibles :** Écrire, enregistrer un message vocal, envoyer une photo d'un carton emballé prêt à partir.
5. **États :** Connecté, en cours de rédaction, message transmis.
6. **Erreurs :** Panne réseau $\rightarrow$ Notification locale et bascule proposée vers un appel direct.
7. **Comportement responsive :** Deux colonnes sur écran large.
8. **Comportement mobile :** Interface légère calquée sur la simplicité de WhatsApp.
9. **Comportement desktop :** Gestion simultanée de plusieurs conversations avec des revendeurs.

---

### ÉCRAN G11 : CARNET DE CLIENTS REVENDEURS
1. **Objectif :** Disposer d'une base de données de ses clients réguliers pour les fidéliser et leur annoncer les nouveaux arrivages de conteneurs.
2. **Composants :** Liste des acheteurs ayant déjà commandé dans la boutique, localisation géographique de chaque client (ex: *Boutique à San Pedro, Revendeur Yopougon Siporex*), volume total de cartons achetés, bouton *"Envoyer une alerte arrivage par WhatsApp"*.
3. **Informations affichées :** Date du dernier achat, articles les plus commandés par ce revendeur.
4. **Actions possibles :** Consulter la fiche client, lancer un appel, envoyer un message de réassort personnalisé.
5. **États :** Liste clients, recherche par nom ou commune.
6. **Erreurs :** Aucune.
7. **Comportement responsive :** Liste sur mobile $\rightarrow$ CRM B2B simplifié sur grand écran.
8. **Comportement mobile :** Appels téléphoniques en 1 clic.
9. **Comportement desktop :** Export de la liste de contacts pour campagnes SMS ciblées.

---

### ÉCRAN G12 : STATISTIQUES COMMERCIALES & PRODUITS STARS
1. **Objectif :** Permettre au chef d'entreprise de comprendre les tendances de vente de son magasin physique et digital.
2. **Composants :** Graphiques visuels simples et lisibles :
   - Évolution hebdomadaire du volume de cartons écoulés.
   - Top 5 des produits les plus recherchés dans son catalogue.
   - Répartition géographique des acheteurs (Abidjan vs Intérieur du pays).
   - Taux de satisfaction client et avis reçus.
3. **Informations affichées :** Chiffres bruts en FCFA, pourcentages d'évolution, notes moyennes.
4. **Actions possibles :** Filtrer par période (*7 derniers jours, Ce mois-ci, Cette année*), exporter les statistiques.
5. **États :** Données à jour, période sans vente.
6. **Erreurs :** Aucune.
7. **Comportement responsive :** Graphiques adaptés vectoriels SVG légers.
8. **Comportement mobile :** Synthèse chiffrée prioritaire sur les graphiques complexes pour consultation rapide.
9. **Comportement desktop :** Graphiques complets interactifs avec infobulles au survol.

---

### ÉCRAN G13 : PARAMÈTRES BOUTIQUE & GESTION DES COMMIS
1. **Objectif :** Configurer les paramètres opérationnels et déléguer l'usage de l'application aux employés de magasin.
2. **Composants :**
   - Coordonnées de paiement Mobile Money professionnel (Wave Marchand, Orange Money Pro, MTN MoMo).
   - Gestion des accès commis : Ajout d'un employé par son numéro de téléphone avec droits restreints (*Préparer les commandes uniquement, sans voir le chiffre d'affaires global*).
   - Heures d'ouverture du magasin.
   - Alertes sonores de nouvelles commandes (sonnerie stridente spéciale pour être audible dans le bruit d'Adjamé).
3. **Informations affichées :** Statut des comptes des employés, numéros de réception des règlements.
4. **Actions possibles :** Ajouter/révoquer un commis, modifier les numéros Mobile Money, tester la sonnerie d'alerte.
5. **États :** Profil administrateur du magasin vs profil commis connecté.
6. **Erreurs :** Numéro de commis invalide $\rightarrow$ Alerte rouge.
7. **Comportement responsive :** Formulaire sur une colonne.
8. **Comportement mobile :** Paramétrage facile des notifications et sonneries d'ambiance bruyante.
9. **Comportement desktop :** Matrice de permissions fines par employé de magasin.

---

### ÉCRAN G14 : ABONNEMENT & BOOSTS DE VISIBILITÉ
1. **Objectif :** Permettre au grossiste d'augmenter ses ventes en souscrivant à des options de visibilité payantes.
2. **Composants :**
   - Présentation des 3 packs :
     - *Gratuit (Freemium)* : Jusqu'à 10 produits, référencement standard.
     - *Pack Top Marchand (25 000 FCFA/mois)* : Produits illimités, badge exclusif "Grossiste Premium", mise en avant en tête des résultats.
     - *Boost "Arrivage Conteneur" (5 000 FCFA par push)* : Notification ciblée envoyée à 5 000 revendeurs de la filière.
   - Bouton de paiement instantané via Mobile Money (Wave, Orange, MTN).
3. **Informations affichées :** Nombre de jours restants sur l'abonnement en cours, estimation du nombre de revendeurs touchés par le boost.
4. **Actions possibles :** Souscrire, renouveler, payer par Mobile Money, consulter les reçus de paiement.
5. **États :** Forfait actif, forfait expiré, paiement en cours.
6. **Erreurs :** Échec de transaction Mobile Money $\rightarrow$ Message invitant à vérifier son solde Wave/Orange.
7. **Comportement responsive :** 3 cartes horizontales sur mobile $\rightarrow$ 3 colonnes tarifaires sur desktop.
8. **Comportement mobile :** Déclenchement transparent du push de paiement Mobile Money sur le téléphone.
9. **Comportement desktop :** Facture téléchargeable avec mention légale pour la comptabilité du grossiste.

---

## 6. BACK-OFFICE & CONSOLE D'ADMINISTRATION (6 ÉCRANS)

Destiné à être utilisé sur ordinateur et tablette par les équipes opérationnelles et les délégués terrain d'Adjamé Market.

---

### ÉCRAN A01 : DASHBOARD NATIONAL DE SUPERVISION
1. **Objectif :** Suivre la santé globale de la marketplace et arbitrer les opérations quotidiennes.
2. **Composants :** Indicateurs globaux (GMV mensuel intermédié en FCFA, nombre de grossistes vérifiés actifs, volume de recherches quotidiennes, taux de conversion en devis, alertes de litiges urgents), carte géographique interactive de la répartition des boutiques dans les sous-marchés d'Adjamé (Forum, Roxy, Gouro, Dallas, etc.).
3. **Informations affichées :** Évolution des KPIs en temps réel, volume d'affaires par catégorie.
4. **Actions possibles :** Filtrer par date/secteur, exporter les rapports, attribuer les tâches aux agents de terrain.
5. **États :** Données en direct actualisées toutes les 60 secondes.
6. **Erreurs :** Aucune.
7. **Comportement responsive :** Optimisé pour résolutions $\ge 1024\text{px}$.
8. **Comportement mobile :** Vue synthétique responsive pour le superviseur en déplacement sur le marché.
9. **Comportement desktop :** Affichage multi-écrans avec graphiques analytiques détaillés.

---

### ÉCRAN A02 : VALIDATION TERRAIN & CERTIFICATION KYC
1. **Objectif :** Valider physiquement les nouvelles boutiques d'Adjamé avant attribution du badge de confiance.
2. **Composants :** Liste des demandes de vérification en attente, visualisateur des photos de pièces d'identité et de bail, formulaire de rapport de visite de l'agent terrain avec géolocalisation GPS vérifiée sur place, boutons *"Approuver et délivrer le badge Or"* / *"Rejeter avec motif"*.
3. **Informations affichées :** Nom du propriétaire, localisation précise, numéro de box, photos prises in situ par l'ambassadeur de terrain.
4. **Actions possibles :** Valider la boutique, demander des photos complémentaires, envoyer l'agent terrain sur place pour contre-visite.
5. **États :** En attente de visite, visite effectuée, boutique certifiée, dossier rejeté.
6. **Erreurs :** Données GPS incohérentes avec Adjamé $\rightarrow$ Blocage de la validation automatique.
7. **Comportement responsive :** Adapté tablette pour les agents de terrain effectuant l'audit sur place.
8. **Comportement mobile :** Formulaire mobile léger pour la saisie directe par l'agent dans les allées du marché.
9. **Comportement desktop :** Double écran de comparaison des documents officiels.

---

### ÉCRAN A03 : MODÉRATION DU CATALOGUE & DES PRIX
1. **Objectif :** Garantir la véracité des prix de gros et éliminer les produits interdits, contrefaçons dangereuses ou doublons parasites.
2. **Composants :** Tableau de tous les articles publiés récemment, filtre algorithmique détectant les prix anormaux (ex: produit vendu 10 fois sous le cours normal = suspicion d'arnaque), boutons *"Approuver"*, *"Suspendre le produit"*, *"Avertir le vendeur"*.
3. **Informations affichées :** Photo, titre, prix carton, historique des signalements éventuels sur cet article.
4. **Actions possibles :** Masquer une annonce, exiger une correction de tarif, bloquer un vendeur récidiviste.
5. **États :** En ligne, Suspendu pour vérification, Supprimé définitivement.
6. **Erreurs :** Aucune.
7. **Comportement responsive :** Tableau avec défilement horizontal fluide sur petit écran.
8. **Comportement mobile :** Actions rapides de modération par balayage.
9. **Comportement desktop :** Modération rapide assistée par raccourcis clavier (A = Approuver, S = Suspendre).

---

### ÉCRAN A04 : GESTION DES CATÉGORIES & SOUS-FILIÈRES
1. **Objectif :** Structurer l'arborescence des filières marchandes selon l'évolution des stocks d'Adjamé.
2. **Composants :** Arbre hiérarchique interactif des catégories et sous-familles, bouton d'ajout d'une filière, sélecteur d'icônes M3, gestionnaire de l'ordre d'affichage sur l'écran d'accueil acheteur.
3. **Informations affichées :** Nombre de produits et de boutiques rattachés à chaque catégorie.
4. **Actions possibles :** Créer, renommer, fusionner ou réordonner les filières marchandes.
5. **États :** Catégorie active / Catégorie masquée temporairement.
6. **Erreurs :** Tentative de suppression d'une catégorie contenant des produits actifs $\rightarrow$ Message exigeant le reclassement préalable des articles.
7. **Comportement responsive :** Vue arborescente standard.
8. **Comportement mobile :** Non recommandé (administration privilégiée sur grand écran).
9. **Comportement desktop :** Glisser-déposer pour réordonner les catégories sur l'application mobile en direct.

---

### ÉCRAN A05 : CONSOLE DE TRAITEMENT DES LITIGES & SIGNALEMENTS
1. **Objectif :** Résoudre les conflits entre acheteurs et vendeurs en moins de 2 heures pour préserver la confiance sur la plateforme.
2. **Composants :** Liste des tickets de signalement ouverts classés par gravité (Rouge = Suspicion d'arnaque sur acompte, Orange = Produit non conforme, Jaune = Magasin difficile à trouver), dossier complet comprenant les coordonnées des deux parties, les échanges et les preuves photos, boutons d'arbitrage : *"Médiation réussie"*, *"Remboursement acompte ordonné"*, *"Exclusion définitive de la boutique"*.
3. **Informations affichées :** Historique de réclamations de l'acheteur et du grossiste concerné.
4. **Actions possibles :** Contacter l'acheteur et le grossiste par téléphone en 1 clic, consigner les conclusions de la médiation, prononcer une sanction.
5. **États :** Nouveau litige, En cours d'instruction, Résolu, Boutique bannie.
6. **Erreurs :** Aucune.
7. **Comportement responsive :** Interface de traitement optimisée pour ordinateur.
8. **Comportement mobile :** Notification push prioritaire envoyée au responsable des opérations dès l'ouverture d'un litige grave.
9. **Comportement desktop :** Vue complète à 360 degrés sur la transaction contestée.

---

### ÉCRAN A06 : BAROMÈTRE HEBDOMADAIRE DES PRIX D'ADJAMÉ
1. **Objectif :** Éditer et publier la synthèse hebdomadaire officielle des cours de gros à destination de la communauté marchande.
2. **Composants :** Outil de calcul automatique du prix moyen constaté par carton sur les références phares (Riz, Huile, Savon, Pagnes, Écouteurs), éditeur de texte pour rédiger le mémo hebdomadaire, bouton de diffusion multicanale (*"Publier sur l'application et envoyer par WhatsApp à 25 000 commerçants"*).
3. **Informations affichées :** Tendances à la hausse ou à la baisse des prix du marché d'Adjamé, causes économiques (cours du dollar, frais de dédouanement portuaire à Abidjan).
4. **Actions possibles :** Valider les cours officiels, programmer la diffusion hebdomadaire, exporter l'infographie partageable sur les réseaux sociaux.
5. **États :** Brouillon en cours de rédaction, baromètre publié.
6. **Erreurs :** Données de prix insuffisantes sur une catégorie $\rightarrow$ Alerte invitant les agents terrain à effectuer un relevé physique.
7. **Comportement responsive :** Éditeur pleine page.
8. **Comportement mobile :** Aperçu du format mobile avant validation de l'envoi WhatsApp.
9. **Comportement desktop :** Outil de mise en page graphique automatique de l'infographie de synthèse.

---

## 7. USER FLOWS CRITIQUES (PARCOURS TYPES DÉTAILLÉS)

### Flow 1 : Recherche Express vers Négociation WhatsApp (Le parcours roi en 4 taps)
```
[Écran 07 : Accueil] 
       | (Tap 1 : Clic barre de recherche)
       v
[Écran 08 : Recherche] 
       | (Saisie "Savon Kanza" + Tap 2 : Sélection suggestion)
       v
[Écran 09 : Résultats] 
       | (Tap 3 : Clic sur le produit avec palier 21 600 F/carton)
       v
[Écran 14 : Fiche Produit] 
       | (Sélection quantité "10 cartons" -> Total calculé : 216 000 FCFA)
       | (Tap 4 : Clic sur "Négocier sur WhatsApp")
       v
[Application WhatsApp Externe]
-> Message pré-rempli généré automatiquement :
   « Bonjour Éts Fanta Beauté, j'ai vu votre article Savon Kanza Éclaircissant
     sur Adjamé Market. Je souhaite commander 10 cartons (au tarif de 21 600 F/ctn).
     Est-ce disponible aujourd'hui à votre magasin de Roxy ? »
```

### Flow 2 : Commande Sécurisée Click & Collect avec Retrait par Code PIN
```
[Écran 14 : Fiche Produit] -> [Écran 15 : Panier B2B]
       |
       v
[Écran 17 : Récapitulatif Commande]
       | (Choix de l'option "Retrait en magasin à Adjamé Forum B-14")
       | (Validation sans paiement immédiat ou acompte Wave)
       v
[Écran 18 : Confirmation & Reçu Numérique]
       | -> Génération instantanée du Code PIN à 4 chiffres : 7 4 8 2
       |
       v
[Passage Physique du Commerçant ou de son livreur au Magasin d'Adjamé]
       |
       v
[Écran G08 : Application du Grossiste]
       | -> Le grossiste prépare le lot de cartons
       | -> L'acheteur présente son écran avec le code PIN "7 4 8 2"
       | -> Le grossiste saisit le code sur son smartphone
       | -> Transaction validée, stock décrémenté, reçu horodaté pour les deux parties
```

### Flow 3 : Publication Express d'un Arrivage Conteneur par le Grossiste (< 60 secondes)
```
[Écran G01 : Dashboard Grossiste]
       | (Clic sur "+ Publier un arrivage")
       v
[Écran G05 : Ajout Rapide]
       | -> 1. Prise de 2 photos du carton ouvert via la caméra du smartphone (15s)
       | -> 2. Dictée vocale : « Carton de chargeurs rapides 30W double port » (10s)
       | -> 3. Clic sur catégorie "Électronique" (3s)
       | -> 4. Saisie prix : "27 500" et MOQ : "2 cartons" (12s)
       | -> 5. Clic sur "Mettre en vente immédiatement" (2s)
       v
[Article en ligne et notifié immédiatement aux 1 200 revendeurs High-Tech d'Abidjan]
```

---

## 8. MATRICE DE COMPORTEMENT MULTI-PLATEFORME & RESPONSIVE

| Composant Majeur | Smartphone (< 600dp) | Tablette / Pliable (600dp - 840dp) | Desktop / Ordinateur (> 840dp) |
| :--- | :--- | :--- | :--- |
| **Barre de Navigation** | Bottom Navigation Bar fixe à 4 icônes (Accueil, Recherche, Favoris, Profil). | Navigation Rail vertical compact à gauche de l'écran. | Header supérieur étendu complet avec profil et accès direct aux outils. |
| **Grille Produits** | 2 colonnes compactes optimisées pour défilement rapide du pouce. | 3 colonnes avec aperçu des paliers de prix directement sur la carte. | 4 à 5 colonnes avec filtres permanents déployés sur le flanc gauche. |
| **Fiche Produit B2B** | Défilement vertical fluide avec barre d'action (WhatsApp/Devis) sticky en bas d'écran. | Disposition à deux colonnes équilibrées (Photos à gauche, tableau des paliers à droite). | Disposition e-commerce B2B avancée avec calculateur de marge revendeur intégré. |
| **Filtres Avancés** | Bottom Sheet modale surgissante avec poignée tactile de glissement. | Boîte de dialogue centrée sur l'écran avec fond obscurci. | Panneau latéral permanent en colonne de gauche sans recouvrement des résultats. |
| **Bouton WhatsApp** | Pleine largeur (Full Width 52dp) toujours sous le pouce. | Largeur fixe 320dp aligné à droite du panneau d'action. | Bouton d'action proéminent avec QR Code WhatsApp Web pour les utilisateurs PC. |

---

Cette spécification UX/UI constitue la charte de référence absolue pour le prototypage, l'intégration des maquettes et le développement frontend d'**Adjamé Market**.
