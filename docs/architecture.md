# Architecture Globale - DevUnity

Ce document décrit l'architecture logicielle de la plateforme **DevUnity**, structurée en microservices modernes, robustes et scalables. 

---

## 1. Vue d'ensemble de l'Architecture

L'application DevUnity repose sur une architecture orientée microservices (Microservices Architecture). Elle sépare les responsabilités métiers en plusieurs services indépendants développés principalement en **Java (Spring Boot 3)** et **Python (FastAPI)**, orchestrés via **Spring Cloud** et sécurisés par **Keycloak**. Le frontend est une Single Page Application (SPA) en **Angular**.

### Diagramme Microservices

```mermaid
graph TD
    %% Entités Externes
    Client[📱 Frontend Angular]
    
    %% Infrastructure Core
    subgraph Infrastructure Core
        Gateway[🚪 API Gateway :8081]
        Eureka[🔍 Eureka Discovery :8761]
        Keycloak[🔐 Keycloak IAM :8080]
    end
    
    %% Microservices Spring Boot
    subgraph Services Métiers (Spring Boot)
        Academic[🎓 Academic Management]
        Activity[🏃 Activity Management]
        Community[🤝 Community Engagement]
        Language[🗣️ Language Courses]
        Learner[👨‍🎓 Learner Management]
        Social[💬 Social Interaction]
    end
    
    %% Intelligence Artificielle
    subgraph Intelligence Artificielle (Python)
        ML[🧠 ML Dropout Service]
    end
    
    %% Bases de données et APIs externes
    subgraph Data & Externes
        MySQL[(🗄️ MySQL - 1 DB/Service)]
        Elastic[(🔎 Elasticsearch)]
        Cloudinary[☁️ Cloudinary]
        Gemini[🤖 Gemini API]
        SMTP[📧 Gmail SMTP]
    end

    %% Flux Frontend
    Client -->|Authentification| Keycloak
    Client -->|Requêtes REST / WS| Gateway
    
    %% Routage Gateway
    Gateway -->|Routage| Academic
    Gateway -->|Routage| Activity
    Gateway -->|Routage| Community
    Gateway -->|Routage| Language
    Gateway -->|Routage| Learner
    Gateway -->|Routage| Social
    Gateway -->|Routage| ML
    
    %% Enregistrement Eureka
    Academic -.->|S'enregistre| Eureka
    Activity -.->|S'enregistre| Eureka
    Community -.->|S'enregistre| Eureka
    Language -.->|S'enregistre| Eureka
    Learner -.->|S'enregistre| Eureka
    Social -.->|S'enregistre| Eureka
    Gateway -.->|Résolution IP| Eureka
    ML -.->|S'enregistre (PyEureka)| Eureka
    
    %% Persistance
    Academic --> MySQL
    Activity --> MySQL
    Community --> MySQL
    Language --> MySQL
    Learner --> MySQL
    Social --> MySQL
    Social --> Elastic
    
    %% Intégrations Externes
    Academic --> Cloudinary
    Activity --> Cloudinary
    Learner --> Gemini
    Language --> SMTP
    Community --> SMTP
```

---

## 2. Composants de l'Infrastructure

### Eureka Discovery (`Discovery_Service`)
- **Rôle** : Registre de services.
- **Fonctionnement** : Chaque microservice (y compris l'API Gateway et le service Python) s'enregistre auprès d'Eureka lors de son démarrage. Cela permet aux services de se découvrir dynamiquement sans connaître leurs adresses IP ou ports codés en dur.

### API Gateway (`API_Gateway`)
- **Rôle** : Point d'entrée unique (Single Point of Entry) pour toutes les requêtes du client.
- **Fonctionnement** : Basé sur Spring Cloud Gateway, il interroge Eureka pour résoudre les noms des services et route les requêtes entrantes vers les instances appropriées (ex: `/learners/api/**` route vers `Learner_Management_Service`). Il gère également les configurations CORS globales.

### Keycloak Authentication
- **Rôle** : Gestionnaire d'identité et d'accès (IAM).
- **Fonctionnement** : Implémente OAuth2 et OpenID Connect (OIDC). Le frontend Angular s'authentifie auprès de Keycloak pour obtenir un token JWT. L'API Gateway et les microservices valident ce JWT (vérification de la signature RSA) avant d'autoriser l'accès aux ressources (`spring.security.oauth2.resourceserver.jwt.issuer-uri`). Le service `Learner_Management` interagit également avec l'API Admin de Keycloak pour provisionner de nouveaux utilisateurs.

---

## 3. Microservices Métiers

1. **Learner Management Service** : Gère les profils des apprenants, l'inscription (via Keycloak API) et intègre un chatbot tuteur propulsé par l'IA Gemini.
2. **Academic Management Service** : Gère les cours, les certifications, les évaluations et stocke les ressources multimédias via Cloudinary.
3. **Activity Management Service** : Gère les événements, les tâches Kanban, et intègre également du stockage Cloudinary.
4. **Language Courses Service** : Gère les offres linguistiques et envoie des notifications e-mail via Gmail SMTP.
5. **Community Engagement Service** : Gère les forums, les clubs étudiants et les notifications e-mail.
6. **Social Interaction Service** : Gère les chats, les messages WebSockets et utilise **Elasticsearch** pour une recherche textuelle rapide et optimisée des messages ou publications.
7. **ML Dropout Service (Python)** : Microservice d'intelligence artificielle développé en Python (FastAPI). S'enregistre sur Eureka via `py_eureka_client` et expose des endpoints pour la prédiction de l'attrition scolaire (churn) et l'analyse de CV.

---

## 4. Communication entre Services

L'architecture favorise un découplage fort. Les communications inter-services s'effectuent de deux manières :
- **Synchrone (REST/OpenFeign)** : Les microservices communiquent entre eux en utilisant des clients Feign (ex: `UserClient` dans Academic Management) en s'appuyant sur Eureka pour la résolution des adresses.
- **WebSockets** : Utilisés pour la communication temps réel avec le client Angular (Notifications, Chat social, rappels Kanban).

---

## 5. Bases de Données (Database per Service)

Suivant les bonnes pratiques des microservices, le pattern **Database per Service** est appliqué :
- Chaque microservice possède sa propre base de données MySQL isolée (ex: `Learner_Management`, `Activity_Management`, etc.). Cela évite les couplages de données et permet une scalabilité indépendante.
- **Elasticsearch** est introduit spécifiquement pour le service Social afin de fournir des capacités de recherche Full-Text performantes.

---

## 6. Flux Utilisateur (User Flow)

1. **Visite et Authentification** :
   - L'utilisateur accède au frontend Angular.
   - S'il n'est pas authentifié, il est redirigé vers l'interface de login Keycloak (ou utilise le formulaire frontend qui appelle l'API de connexion/enregistrement).
   - Lors de l'inscription, `Learner_Management_Service` crée l'utilisateur dans Keycloak en appelant l'API Admin.
2. **Navigation et Requêtes** :
   - L'application Angular reçoit un token JWT.
   - Angular inclut ce token dans le header `Authorization: Bearer <token>` de chaque requête HTTP vers l'API Gateway.
3. **Routage et Sécurité** :
   - La Gateway route la requête vers le microservice approprié.
   - Le microservice intercepte la requête, valide le token JWT auprès du Realm Keycloak, et vérifie les rôles (ex: `ROLE_STUDENT`, `ROLE_ADMIN`).
4. **Exécution et Réponse** :
   - Le microservice exécute la logique métier (lecture/écriture MySQL, appel API Cloudinary/Gemini).
   - La réponse HTTP est renvoyée à la Gateway, puis au client Angular.
