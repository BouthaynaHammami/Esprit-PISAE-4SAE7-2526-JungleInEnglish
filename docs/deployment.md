# 🚀 Déploiement & CI/CD — DevUnity

> **Rôle du document :** Ce document détaille les stratégies de déploiement de la plateforme DevUnity, de l'environnement de développement local jusqu'à la production via Kubernetes et l'intégration continue.

---

## 1. Pipeline CI/CD (Jenkins)

DevUnity utilise **Jenkins** pour orchestrer l'intégration et le déploiement continus. Le code source contient deux pipelines distincts :

### 🛠️ Intégration Continue (`Jenkinsfile_CI`)
Ce pipeline se déclenche à chaque Push ou Pull Request sur les branches principales.
1. **Checkout** : Récupération du code source.
2. **Build Maven** : Compilation des microservices Java (`mvn clean install`).
3. **Tests Unitaires** : Exécution des tests automatisés.
4. **Analyse de Code Statistique** : 
   - **SonarQube** : Vérification de la qualité du code (Bugs, Vulnérabilités, Code Smells, Couverture de test).
   - **Semgrep** : Analyse de sécurité avancée.
5. **Quality Gate** : Échec du pipeline si les critères de qualité ne sont pas atteints.

### 📦 Déploiement Continu (`Jenkinsfile_CD`)
Ce pipeline prend le relais après une CI réussie (généralement sur la branche `main`).
1. **Build Docker Images** : Création d'une image Docker pour chaque microservice.
2. **Push Docker Registry** : Envoi des images vers un registre Docker privé ou DockerHub.
3. **Deploy to Kubernetes** : Mise à jour des manifestes K8s (Rolling Update).

---

## 2. Conteneurisation (Docker)

Chaque composant (Frontend, Gateway, Eureka, Microservices Java, Service ML Python) possède son propre `Dockerfile`.

- **Build Multi-stage (Java)** : Un stage pour compiler avec Maven, un stage pour exécuter avec un JRE léger (ex: `eclipse-temurin:17-jre-alpine`), réduisant la taille des images finales.
- **Docker Compose** : Un fichier `docker-compose.yml` (à la racine) permet de monter toute l'infrastructure (MySQL, Keycloak) et les services pour le développement local d'une seule commande :
  ```bash
  docker compose up -d
  ```

---

## 3. Orchestration (Kubernetes)

Pour la production et le staging, DevUnity est déployé sur un cluster **Kubernetes**. Le dossier `k8s/` contient les manifestes nécessaires.

### Composants K8s :
- **Deployments** : Gèrent les pods des microservices. Permettent le scaling horizontal (plusieurs instances d'un même service) et l'auto-healing.
- **Services (ClusterIP)** : Exposent les microservices en interne pour qu'ils soient joignables par l'API Gateway.
- **Ingress Controller** : Expose l'API Gateway et le Frontend Angular au trafic internet externe (gestion SSL/TLS).
- **ConfigMaps & Secrets** : Gèrent les variables d'environnement (`DB_PASSWORD`, `JWT_SECRET`) hors du code source.

---

## 4. Gestion de la Configuration Sécurisée

Il est **strictement interdit** de stocker des mots de passe dans les fichiers de configuration versionnés (`application.properties`).

En production, l'injection se fait via les variables d'environnement :
1. Les `application.properties` utilisent la syntaxe `${VAR_NAME:default_value}`.
2. Les conteneurs reçoivent ces variables via les Kubernetes Secrets.
3. En développement local, un fichier `.env.local` (ignoré par Git) est utilisé avec Docker Compose.
