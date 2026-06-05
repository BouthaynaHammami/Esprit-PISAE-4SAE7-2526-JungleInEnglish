# 🔐 Sécurité — DevUnity Platform

## Architecture de sécurité

DevUnity utilise **Keycloak** comme système centralisé d'Identity & Access Management (IAM) basé sur le standard **OAuth2 / OpenID Connect**.

```
Angular Frontend
      │
      │ (1) Login → Keycloak
      │ (2) Reçoit Access Token (JWT)
      │
      ▼
API Gateway ──→ valide JWT (JWKS) ──→ Microservices
```

---

## Configuration Keycloak

### Realm

| Paramètre | Valeur |
|-----------|--------|
| Realm Name | `devunity` |
| Fichier d'import | `Back-PIDEV-4SAE7-2026-DEVUNITY/keycloak-realm.json` |

### Clients

| Client ID | Type | Utilisé par |
|-----------|------|-------------|
| `devunity-app` | Public | Frontend Angular |
| `devunity-service` | Confidential | Backend microservices |

### Rôles

| Rôle | Permissions |
|------|-------------|
| `ADMIN` | Accès complet à toutes les fonctionnalités |
| `TUTOR` | Gestion des cours, consultations étudiants |
| `STUDENT` | Accès apprentissage, postulation offres |
| `EMPLOYEE` | Gestion recrutement, offres |
| `PARENT` | Consultation des données enfants |

---

## Configuration par microservice

Chaque microservice Spring Boot valide les tokens JWT via JWKS :

```properties
# application.properties (chaque service)
spring.security.oauth2.resourceserver.jwt.issuer-uri=\
  ${KEYCLOAK_ISSUER_URI:http://localhost:8080/realms/devunity}
```

---

## Variables d'environnement requises

```bash
KEYCLOAK_ISSUER_URI=http://localhost:8080/realms/devunity
KEYCLOAK_URL=http://localhost:8080
KEYCLOAK_REALM=devunity
KEYCLOAK_CLIENT_ID=devunity-app
KEYCLOAK_ADMIN_USERNAME=admin
KEYCLOAK_ADMIN_PASSWORD=<votre_mot_de_passe>
```

> ⚠️ Ne jamais committer ces valeurs en clair dans `application.properties`.

---

## Bonnes pratiques appliquées

- ✅ Tokens JWT signés par Keycloak (RS256)
- ✅ Validation JWKS côté service (pas de clé secrète partagée)
- ✅ CORS géré centralement par l'API Gateway
- ✅ HTTPS recommandé en production (reverse proxy Nginx/Traefik)
- ✅ App Passwords Gmail (pas le mot de passe principal)
- ✅ Secrets via variables d'environnement (`.env.example` fourni)
