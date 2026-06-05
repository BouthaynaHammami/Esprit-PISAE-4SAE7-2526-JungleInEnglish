# 🚀 Guide d'Installation — DevUnity Platform

## Prérequis

| Outil | Version minimale | Vérification |
|-------|-----------------|--------------|
| Java (JDK) | 17+ | `java -version` |
| Maven | 3.8+ | `mvn -version` |
| Node.js | 18+ | `node -version` |
| npm | 9+ | `npm -version` |
| Python | 3.9+ | `python --version` |
| Docker | 24+ | `docker -version` |
| MySQL | 8.0+ | `mysql --version` |
| Git | 2.x | `git --version` |

---

## 1. Cloner le dépôt

```bash
git clone https://github.com/saifelislem/Esprit-PIDEV-4SAE7-2026-DEVUNITY.git
cd Esprit-PIDEV-4SAE7-2026-DEVUNITY
```

---

## 2. Configuration des variables d'environnement

```bash
# Copier le fichier exemple
cp .env.example .env.local

# Éditer les valeurs dans .env.local
# (ne jamais committer ce fichier)
```

Variables minimales à configurer :

```properties
DB_PASSWORD=votre_mdp_mysql
CLOUDINARY_CLOUD_NAME=...
CLOUDINARY_API_KEY=...
CLOUDINARY_API_SECRET=...
MAIL_USERNAME=...
MAIL_APP_PASSWORD=...
GEMINI_API_KEY=...
```

---

## 3. Infrastructure

### MySQL

Créer les bases de données :

```sql
CREATE DATABASE IF NOT EXISTS Academic_Management;
CREATE DATABASE IF NOT EXISTS Activity_Management;
CREATE DATABASE IF NOT EXISTS Learner_Management;
CREATE DATABASE IF NOT EXISTS Language_Courses;
CREATE DATABASE IF NOT EXISTS Community_Engagement;
CREATE DATABASE IF NOT EXISTS Social_Interaction;
```

### Keycloak

```bash
# Via Docker
docker run -d \
  --name keycloak \
  -p 8080:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:latest start-dev

# Importer le realm DevUnity
# Aller sur http://localhost:8080/admin → Import → keycloak-realm.json
```

---

## 4. Backend — Services Java (Spring Boot)

### Ordre de démarrage

```bash
# 1. Discovery Service (Eureka)
cd Back-PIDEV-4SAE7-2026-DEVUNITY/Discovery_Service
mvn spring-boot:run

# 2. API Gateway
cd ../API_Gateway
mvn spring-boot:run

# 3. Microservices métier (dans n'importe quel ordre)
cd ../Academic_Management_Service && mvn spring-boot:run &
cd ../Activity_Management_Service && mvn spring-boot:run &
cd ../Learner_Management_Service && mvn spring-boot:run &
cd ../Language_Courses_Service && mvn spring-boot:run &
cd ../Community_Engagement_Service && mvn spring-boot:run &
cd ../Social_Interaction_Service && mvn spring-boot:run &
```

### Vérification

- Eureka Dashboard : http://localhost:8761
- Tous les services doivent apparaître comme `UP`

---

## 5. ML Service (Python FastAPI)

```bash
cd Back-PIDEV-4SAE7-2026-DEVUNITY/ML_Dropout_Service

# Créer un environnement virtuel
python -m venv venv
source venv/bin/activate        # Linux/Mac
# ou
.\venv\Scripts\activate         # Windows

# Installer les dépendances
pip install -r requirements.txt

# Démarrer le service
python app/main.py
# → accessible sur http://localhost:8090
# → Swagger : http://localhost:8090/docs
```

---

## 6. Frontend Angular

```bash
cd Front-PIDEV-4SAE7-2026-DEVUNITY

# Installer les dépendances
npm install

# Démarrer le serveur de développement
npm start
# → accessible sur http://localhost:4200
```

---

## 7. Démarrage avec Docker Compose (optionnel)

```bash
# Depuis la racine du projet
docker compose up --build
```

---

## 8. Vérification finale

| Service | URL | Résultat attendu |
|---------|-----|-----------------|
| Frontend Angular | http://localhost:4200 | Page d'accueil DevUnity |
| API Gateway | http://localhost:8081 | 200 OK |
| Eureka | http://localhost:8761 | Dashboard (tous services UP) |
| Keycloak | http://localhost:8080 | Console d'administration |
| ML Swagger | http://localhost:8090/docs | FastAPI Swagger UI |
| Swagger UI | http://localhost:8081/webjars/swagger-ui/index.html | API Docs agrégées |
