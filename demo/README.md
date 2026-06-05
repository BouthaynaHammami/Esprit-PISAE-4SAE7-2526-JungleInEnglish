# DevUnity

## Description
DevUnity est une plateforme E-Learning et de mise en relation professionnelle de nouvelle génération. Le projet vise à combler le fossé entre l'apprentissage académique et le recrutement professionnel. En intégrant l'Intelligence Artificielle et le Machine Learning, DevUnity offre des parcours d'apprentissage personnalisés, une détection précoce du décrochage scolaire, et un filtrage automatisé des CV pour les recruteurs. C'est un écosystème complet pour les étudiants, les tuteurs et les entreprises.

## Architecture
DevUnity repose sur une architecture **Microservices** robuste et évolutive, orchestrée via Spring Cloud.
- **Frontend** : Application cliente unique Angular communiquant avec l'API Gateway.
- **Infrastructure** : 
  - *API Gateway* : Point d'entrée, routage et gestion CORS.
  - *Eureka Discovery* : Annuaire de découverte et d'enregistrement des services.
  - *Keycloak* : Fournisseur d'identité (IAM) pour l'authentification OAuth2/JWT centralisée.
- **Microservices Métier** : 6 services Spring Boot indépendants avec leur propre base de données.
- **Service IA** : Microservice Python (FastAPI) dédié aux inférences de Machine Learning et à l'analyse NLP.

## Technologies utilisées
- **Frontend** : Angular 18, Tailwind CSS, RxJS, TypeScript.
- **Backend (Java)** : Java 17, Spring Boot 3.2.x, Spring Cloud, Spring Data JPA, OpenFeign, WebSockets.
- **Intelligence Artificielle** : Python 3.9, FastAPI, Scikit-learn (Machine Learning), Google Gemini API (LLM).
- **Sécurité** : Keycloak (OAuth2, OpenID Connect).
- **Base de données** : MySQL 8.0+.
- **Stockage Médias** : Cloudinary (CDN).
- **DevOps & Déploiement** : Docker, Docker Compose, Kubernetes, Jenkins (CI/CD), SonarQube, Semgrep.

## Fonctionnalités
- 🎓 **Gestion Académique** : Création de cours, gestion des modules, quiz interactifs et inscriptions.
- 💼 **Insertion Professionnelle** : Publication d'offres de stage/emploi, dépôt de candidatures, interface recruteur automatisée.
- 🤖 **Intelligence Artificielle** : 
  - Prédiction du risque de décrochage scolaire (Machine Learning).
  - Analyse sémantique et scoring de CV (NLP).
  - Assistant virtuel de tutorat par chat (Google Gemini).
- 🌍 **Cours de Langues** : Modules spécialisés (Business English, anglais pour enfants) et challenges d'évaluation.
- 🤝 **Vie Communautaire** : Gestion des clubs étudiants, adhésions et événements.
- 💬 **Interaction Sociale** : Messagerie instantanée en temps réel (WebSockets) et gestion des notifications.

## Structure du projet
```text
Esprit-PIDEV-4SAE7-2026-DEVUNITY/
├── Back-PIDEV-4SAE7-2026-DEVUNITY/
│   ├── API_Gateway/                  # Point d'entrée (Port 8081)
│   ├── Discovery_Service/            # Annuaire Eureka (Port 8761)
│   ├── Academic_Management_Service/  # Gestion des cours (Port 8086)
│   ├── Activity_Management_Service/  # Recrutement & Événements (Port 8084)
│   ├── Community_Engagement_Service/ # Clubs étudiants (Port 8083)
│   ├── Language_Courses_Service/     # Langues & Challenges (Port 8087)
│   ├── Learner_Management_Service/   # Profils & Prédiction (Port 8085)
│   ├── Social_Interaction_Service/   # Chat temps-réel (Port 8082)
│   ├── ML_Dropout_Service/           # IA Python FastAPI (Port 8090)
│   └── keycloak-realm.json           # Configuration IAM
├── Front-PIDEV-4SAE7-2026-DEVUNITY/  # Application Angular 18
├── docs/                             # Documentation technique détaillée
├── demo/                             # Captures d'écran et vidéos
├── CONTRIBUTING.md                   # Guide de contribution
└── .env.example                      # Modèle des variables d'environnement
```

## Prérequis
Pour exécuter ce projet localement, assurez-vous de disposer des outils suivants :
- **Java JDK 17** ou supérieur.
- **Node.js 18+** et **npm 9+**.
- **Python 3.9+**.
- **Docker Desktop** (recommandé pour exécuter Keycloak et MySQL simplement).
- **MySQL Server 8.0+** (si vous n'utilisez pas la version Docker).
- **Git** et **Maven**.

## Installation
1. Clonez le dépôt sur votre machine locale :
   ```bash
   git clone https://github.com/saifelislem/Esprit-PIDEV-4SAE7-2026-DEVUNITY.git
   cd Esprit-PIDEV-4SAE7-2026-DEVUNITY
   ```
2. Configurez votre environnement de développement (IntelliJ IDEA recommandé pour le backend, VS Code pour le frontend).

## Variables d'environnement
Le projet nécessite la configuration de variables d'environnement (mots de passe, clés API Cloudinary, Gemini, SMTP, etc.).
1. Copiez le fichier modèle situé à la racine du projet :
   ```bash
   cp .env.example .env.local
   ```
2. Éditez le fichier `.env.local` pour y insérer vos propres valeurs.
*(Note : Ne committez jamais de fichier contenant vos vrais secrets sur le dépôt).*

## Base de données
Le projet utilise une approche "Database-per-service". Si vous utilisez une installation locale de MySQL, vous devez créer les bases de données suivantes manuellement avant de lancer les services :
```sql
CREATE DATABASE Academic_Management;
CREATE DATABASE Activity_Management;
CREATE DATABASE Learner_Management;
CREATE DATABASE Language_Courses;
CREATE DATABASE Community_Engagement;
CREATE DATABASE Social_Interaction;
```
*(L'ORM Hibernate se chargera de créer les tables automatiquement au premier lancement).*

## Lancement local
Suivez cet ordre précis pour démarrer la plateforme en mode développement :

**1. Infrastructure de base**
- Lancez votre serveur MySQL.
- Lancez Keycloak via Docker et importez le realm fourni :
  ```bash
  docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:latest start-dev
  ```
  *(Ouvrez `http://localhost:8080/admin` et importez le fichier `Back-PIDEV-4SAE7-2026-DEVUNITY/keycloak-realm.json`)*.

**2. Services de Découverte et Routage**
- Dans `Back-PIDEV-4SAE7-2026-DEVUNITY/Discovery_Service`, exécutez : `mvn spring-boot:run`
- Dans `Back-PIDEV-4SAE7-2026-DEVUNITY/API_Gateway`, exécutez : `mvn spring-boot:run`

**3. Microservices Métier (Java)**
Lancez les 6 microservices métier dans des terminaux séparés via `mvn spring-boot:run` :
- `Academic_Management_Service`
- `Activity_Management_Service`
- `Learner_Management_Service`
- `Language_Courses_Service`
- `Community_Engagement_Service`
- `Social_Interaction_Service`

**4. Service d'Intelligence Artificielle (Python)**
```bash
cd Back-PIDEV-4SAE7-2026-DEVUNITY/ML_Dropout_Service
python -m venv venv
# Activer l'environnement virtuel (selon votre OS) :
# Windows : venv\Scripts\activate
# Linux/Mac : source venv/bin/activate
pip install -r requirements.txt
python app/main.py
```

**5. Frontend (Angular)**
```bash
cd Front-PIDEV-4SAE7-2026-DEVUNITY
npm install
npm start
```
L'application sera accessible sur `http://localhost:4200`.

## Lancement Docker
*(Optionnel, si configuré)*
Pour lancer l'ensemble de l'infrastructure conteneurisée :
```bash
docker compose --env-file .env.local up --build -d
```
L'application frontend sera accessible sur `http://localhost:4200` et les APIs via l'API Gateway sur le port `8081`.

## Démonstration
- Des captures d'écran et des vidéos de démonstration de la plateforme en action sont disponibles dans le dossier [`demo/`](./demo).
- La documentation technique approfondie (Architecture détaillée, Référence API REST, CI/CD, Sécurité) se trouve dans le dossier [`docs/`](./docs).

## Auteurs
Plateforme conçue et développée par la **DevUnity Team** — *Esprit School of Engineering, Promotion 2026*.
