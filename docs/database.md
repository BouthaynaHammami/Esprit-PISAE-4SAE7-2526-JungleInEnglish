# 🗄️ Architecture des Bases de Données — DevUnity

> **Rôle du document :** Décrire la stratégie de persistance des données du projet DevUnity. Il détaille le choix du SGBD, l'approche par microservice, et les schémas de base de données.

---

## 1. Stratégie "Database per Service"

DevUnity applique strictement le pattern **Database per Service** (une base de données par microservice). 

### Avantages de cette approche :
- **Isolation** : Un service ne peut pas bloquer la base de données d'un autre service (pas de Single Point of Failure au niveau DB).
- **Couplage faible** : Empêche les services de contourner les APIs en lisant directement les tables des autres.
- **Évolutivité** : Permet de scaler ou de changer de technologie de base de données pour un service spécifique si besoin.

### Inconvénient géré :
- Les jointures entre données appartenant à différents services sont impossibles au niveau SQL. Elles sont résolues côté application via l'agrégation de requêtes REST (OpenFeign).

---

## 2. Technologies Utilisées

- **SGBD Principal** : MySQL 8+
- **ORM** : Spring Data JPA (Hibernate)
- **Migration de Schéma** : Auto-génération Hibernate (`spring.jpa.hibernate.ddl-auto=update` en dev, script manuel recommandé en prod).

---

## 3. Schémas et Bases de Données

Afin de démarrer l'ensemble du projet, les bases de données suivantes doivent exister dans l'instance MySQL :

| Microservice | Nom de la base de données | Entités principales (exemples) |
|--------------|---------------------------|--------------------------------|
| **Academic Management** | `Academic_Management` | Course, Module, Quiz, Enrollment |
| **Activity Management** | `Activity_Management` | JobOffer, Application, ProfessionalEvent |
| **Learner Management** | `Learner_Management` | StudentProfile, Tutor, DropoutPrediction |
| **Language Courses** | `Language_Courses` | LanguageCourse, EnglishChallenge |
| **Community Engagement** | `Community_Engagement` | Club, ClubMembership, CommunityEvent |
| **Social Interaction** | `Social_Interaction` | ChatRoom, Message, Notification |

> 💡 **Création initiale** : L'option `?createDatabaseIfNotExist=true` dans l'URL JDBC gère la création automatique lors du premier lancement.

---

## 4. Gestion des Fichiers et Médias

Pour éviter d'alourdir les bases de données et de compliquer le déploiement des conteneurs, **aucun fichier lourd n'est stocké en base ou sur le disque local** des microservices (le dossier `uploads/` est ignoré).

- **Cloudinary** : Utilisé comme CDN et stockage cloud pour toutes les images de profil, supports de cours, miniatures de clubs, etc. Les URL Cloudinary sont stockées en base (Type `String`).

---

## 5. Interactions Optionnelles (NoSQL)

Pour le **Social Interaction Service**, bien que MySQL soit la base principale actuelle, une infrastructure **Elasticsearch** est préparée (bien que commentée dans la configuration actuelle) pour permettre la recherche plein texte performante dans l'historique des messages.
