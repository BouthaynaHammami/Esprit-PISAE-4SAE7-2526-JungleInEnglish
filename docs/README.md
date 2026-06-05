# 📖 Documentation Technique — DevUnity Platform

Bienvenue dans la documentation officielle du projet **DevUnity**, structurée selon les standards de publication ESPRIT.

## 📁 Index de la Documentation

| Document | Description / Rôle |
|----------|--------------------|
| 🏗️ [**architecture.md**](architecture.md) | Vue d'ensemble du système, diagrammes, choix du pattern microservices et flux de communication globaux. |
| 🧩 [**microservices.md**](microservices.md) | Description détaillée de chaque microservice (port, domaine, responsabilités) et de leurs dépendances. |
| 🔌 [**api.md**](api.md) | Référence centrale des endpoints REST exposés par les services, avec méthodes, chemins et sécurisation. |
| 🗄️ [**database.md**](database.md) | Architecture de persistance, pattern "Database per Service", SGBD utilisés et gestion des médias. |
| 🚀 [**deployment.md**](deployment.md) | Stratégies de déploiement, pipelines CI/CD Jenkins, conteneurisation Docker et orchestration Kubernetes. |

---

## 🔗 Liens Rapides (Environnement Local)

- **Application Frontend** : http://localhost:4200
- **API Gateway** : http://localhost:8081
- **Discovery Service (Eureka)** : http://localhost:8761
- **Identity Provider (Keycloak)** : http://localhost:8080/admin
- **Documentation API Globalisée (Swagger)** : http://localhost:8081/webjars/swagger-ui/index.html
