# 🎓 DevUnity Platform

> A comprehensive learning and community engagement platform based on microservices architecture

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Backend Services](#backend-services)
- [Frontend](#frontend)
- [Testing](#testing)
- [Deployment](#deployment)
- [External Integrations](#external-integrations)
- [Contributing](#contributing)

---

## 📌 Project Overview

**DevUnity** is a comprehensive educational platform offering:

✅ **Academic Management** - Courses, quizzes, tests with fraud detection  
✅ **Language Learning** - English courses for children  
✅ **Activity Management** - Scheduling, calendars, room management  
✅ **User Profiles** - Learner management (Keycloak integration)  
✅ **Community Engagement** - Events and group activities  
✅ **Social Interaction** - Bidirectional messaging with Elasticsearch  
✅ **Interactive Games** - Word battles, collaborative stories (WebSocket)  

**Version**: 1.0-SNAPSHOT  
**Group**: `tn.esprit`  
**Artifact**: `Dev_Unity`

---

## 🏗️ Architecture

### Technology Stack Overview

```
┌─────────────────────────────────────────────────┐
│          Frontend (Angular 18.2.0)              │
│       Localhost:4200 with WebSocket             │
└──────────────┬──────────────────────────────────┘
               │
┌──────────────▼──────────────────────────────────┐
│  API Gateway (Spring Cloud Gateway)             │
│         Port 8081 - Centralized Routing          │
└──────────────┬──────────────────────────────────┘
               │
    ┌──────────┼──────────┬──────────┬────────────┐
    │          │          │          │            │
    ▼          ▼          ▼          ▼            ▼
 Service    Service    Service    Service      Service
Discovery  Academic    Learner    Activity    Community
Eureka     Management  Management Management  Engagement
8761       8086        8082       8084        8087
    │          │          ▼          │            │
    │          │      Social Inter.   │            │
    │          │      (8085)          │            │
    └──────────┴──────────┬───────────┴────────────┘
               │
    ┌──────────┴──────────────┐
    │                         │
    ▼                         ▼
MySQL Database         Elasticsearch
(8 schemas)            (Message Search)
```

### Microservices

| Service | Port | Database | Responsibilities |
|---------|------|----------|------------------|
| **API Gateway** | 8081 | - | Centralized request routing |
| **Discovery Service** (Eureka) | 8761 | - | Service registry, discovery |
| **Learner Management** | 8082 | `Learner_Management` | User profiles, authentication |
| **Language Courses** | 8083 | `Language_Courses` | Language courses for children |
| **Activity Management** | 8084 | `Activity_Management` | Planning, calendars |
| **Social Interaction** | 8085 | `Social_Interaction` | Messaging, social features |
| **Academic Management** | 8086 | `Academic_Management` | Courses, tests, quizzes, certificates |
| **Community Engagement** | 8087 | `Community_Engagement` | Events, community activities |

---

## 📦 Prerequisites

### Backend

- **Java 17+** (JDK 17 or higher)
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git**

### Frontend

- **Node.js 18.19+** and **npm 9.0+**
- **Angular CLI 18.2.21**

### External Services

- **Keycloak 24.0** (http://localhost:8080) - Authentication server
- **Elasticsearch 8.x** (https://localhost:9200) - Message search
- **Optional**: Cloudinary, Gmail SMTP

---

## 🚀 Installation

### 1. Clone the repository

```bash
git clone <repository-url>
cd PI
```

### 2. MySQL Database Configuration

```bash
# Create databases (optional, auto-created by Hibernate)
# MySQL connections are configured in each service's application.properties
```

**Default MySQL credentials** (configure in application.properties):
- Username: `root`
- Host: `localhost:3306`
- Password: **Set in environment variables or properties file** (not hardcoded)

### 3. Start Infrastructure Services

#### 3.1 Start Keycloak

```bash
# Docker (recommended)
docker run -p 8080:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=<secure-password> \
  quay.io/keycloak/keycloak:24.0 \
  start-dev
```

Import realm configuration:
- Access http://localhost:8080/admin
- Import file: `Back-PIDEV-4SAE7-2026-DEVUNITY/keycloak-realm.json`
- Realm: `devunity`
- Client: `devunity-app`

#### 3.2 Start Elasticsearch (optional, for Social Service)

```bash
# Docker
docker run -p 9200:9200 \
  -e discovery.type=single-node \
  -e xpack.security.enabled=false \
  docker.elastic.co/elasticsearch/elasticsearch:8.10.0
```

### 4. Backend Installation and Startup

```bash
cd Back-PIDEV-4SAE7-2026-DEVUNITY

# Compile all services
mvn clean install

# Or compile without tests (faster)
mvn clean install -DskipTests
```

**Start the services** (in separate terminals, in this order):

```bash
# Terminal 1: Discovery Service (Eureka)
java -jar Discovery_Service/target/Discovery_Service-1.0-SNAPSHOT.jar

# Terminal 2: API Gateway
java -jar API_Gateway/target/API_Gateway-1.0-SNAPSHOT.jar

# Terminal 3: Learner Management Service
java -jar Learner_Management_Service/target/Learner_Management_Service-1.0-SNAPSHOT.jar

# Terminal 4: Academic Management Service
java -jar Academic_Management_Service/target/Academic_Management_Service-1.0-SNAPSHOT.jar

# Terminal 5: Language Courses Service
java -jar Language_Courses_Service/target/Language_Courses_Service-1.0-SNAPSHOT.jar

# Terminal 6: Activity Management Service
java -jar Activity_Management_Service/target/Activity_Management_Service-1.0-SNAPSHOT.jar

# Terminal 7: Social Interaction Service
java -jar Social_Interaction_Service/target/Social_Interaction_Service-1.0-SNAPSHOT.jar

# Terminal 8: Community Engagement Service
java -jar Community_Engagement_Service/target/Community_Engagement_Service-1.0-SNAPSHOT.jar
```

> **Tip**: Create a batch/shell script or use Maven parallelization to start all services at once.

### 5. Frontend Installation and Startup

```bash
cd Front-PIDEV-4SAE7-2026-DEVUNITY

# Install dependencies
npm install

# Start development server
ng serve

# Application accessible at: http://localhost:4200
```

**For production** (SSR):

```bash
npm run build
npm run serve:ssr:DevUnity-FrontEnd
```

---

## ⚙️ Configuration

### Backend Configuration (`application.properties`)

All services inherit from a parent configuration:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/{ServiceName}?createDatabaseIfNotExist=true
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Service Discovery (Eureka)
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
eureka.instance.prefer-ip-address=true
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true

# Keycloak / OAuth2
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/devunity
keycloak.auth-server-url=http://localhost:8080
keycloak.realm=devunity
keycloak.resource=devunity-app
keycloak.admin.username=${KEYCLOAK_ADMIN_USER:admin}
keycloak.admin.password=${KEYCLOAK_ADMIN_PASSWORD}

# Logging
logging.level.root=INFO
logging.level.org.springframework.cloud.gateway=DEBUG
logging.level.org.springframework.security=DEBUG
```

### Setting Environment Variables

**Linux/Mac**:
```bash
export DB_USERNAME=root
export DB_PASSWORD=<your-secure-password>
export KEYCLOAK_ADMIN_PASSWORD=<keycloak-password>
```

**Windows PowerShell**:
```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="<your-secure-password>"
$env:KEYCLOAK_ADMIN_PASSWORD="<keycloak-password>"
```

**Windows Command Prompt**:
```batch
set DB_USERNAME=root
set DB_PASSWORD=<your-secure-password>
set KEYCLOAK_ADMIN_PASSWORD=<keycloak-password>
```

### Frontend Configuration (`src/environments/environment.ts`)

```typescript
export const environment = {
  production: false,
  apiGatewayUrl: 'http://localhost:8081',
  wsUrl: 'ws://localhost:8081/ws',
  keycloakUrl: 'http://localhost:8080',
};
```

### Service-Specific Notes

- **Academic Management**: Requires Cloudinary and Gmail SMTP env vars
- **Social Interaction**: Requires Elasticsearch with SSL
- **API Gateway**: CORS pre-configured for `http://localhost:4200`

---

## 📖 Usage

### API Gateway Routes

All requests go through `http://localhost:8081`:

```
/learners/api/**    → Learner Management Service   (8082)
/academics/api/**   → Academic Management Service  (8086)
/languages/api/**   → Language Courses Service     (8083)
/activities/api/**  → Activity Management Service  (8084)
/socials/api/**     → Social Interaction Service   (8085)
/community/api/**   → Community Engagement Service (8087)
```

### Authentication

1. User logs in via Keycloak
2. JWT token is issued
3. Include JWT in every request: `Authorization: Bearer <token>`
4. API Gateway validates and propagates the JWT

### WebSocket (Real-time)

- **URL**: `ws://localhost:8081/ws`
- **Protocol**: SockJS + STOMP
- **Used for**: Interactive games, real-time notifications

### Example Requests

```bash
# Get all courses
curl -H "Authorization: Bearer <token>" \
  http://localhost:8081/academics/api/courses

# Create a course
curl -X POST http://localhost:8081/academics/api/courses \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Spring Boot 101","description":"..."}'
```

---

## 📁 Project Structure

```
PI/
├── Back-PIDEV-4SAE7-2026-DEVUNITY/
│   ├── pom.xml                          # Parent POM
│   ├── keycloak-realm.json              # Keycloak realm config
│   ├── TESTING_DOCUMENTATION.md
│   ├── Discovery_Service/               # Eureka (8761)
│   ├── API_Gateway/                     # API Gateway (8081)
│   ├── Learner_Management_Service/      # Profiles (8082)
│   ├── Language_Courses_Service/        # Languages (8083)
│   ├── Activity_Management_Service/     # Activities (8084)
│   ├── Social_Interaction_Service/      # Messaging (8085)
│   ├── Academic_Management_Service/     # Academic (8086)
│   ├── Community_Engagement_Service/    # Events (8087)
│   └── uploads/                         # File storage
│
├── Front-PIDEV-4SAE7-2026-DEVUNITY/
│   ├── package.json
│   ├── angular.json
│   ├── tsconfig.json
│   ├── TESTING_DOCUMENTATION.md
│   └── src/
│       ├── app/
│       │   ├── core/                    # Services, guards, interceptors
│       │   ├── features/                # Feature modules
│       │   ├── app.module.ts
│       │   └── app-routing.module.ts
│       ├── environments/
│       ├── styles.scss
│       └── index.html
│
└── README.md
```

---

## 🔧 Backend Services

### 1️⃣ Learner Management Service (8082)

**Responsibilities**: User profile management and authentication

**Features**:
- User profile CRUD
- OAuth2 integration with Keycloak
- Role and permission management

**Key Endpoints**:
```
GET    /learners/api/users/{id}
POST   /learners/api/users
PUT    /learners/api/users/{id}
DELETE /learners/api/users/{id}
```

---

### 2️⃣ Academic Management Service (8086)

**Responsibilities**: Complete course and assessment management

**Features**:
- 📚 Course management with visibility control
- 👥 Enrollment system with duplicate prevention
- ❓ Quizzes and questions
- ✅ Tests with fraud detection (tab-switching detection)
- 📊 Automatic grading
- 🎓 Certificate generation with QR codes
- 📧 Email notifications

**External Integrations**: Cloudinary (images), Gmail SMTP (email)

**Key Endpoints**:
```
GET    /academics/api/courses
POST   /academics/api/courses
GET    /academics/api/courses/{id}
GET    /academics/api/enrollments
POST   /academics/api/enrollments
GET    /academics/api/tests/{id}/start
POST   /academics/api/tests/{id}/submit
```

---

### 3️⃣ Language Courses Service (8083)

**Responsibilities**: Specialized language courses for children

**Features**:
- English courses for children
- Course CRUD with visibility management

**Key Endpoints**:
```
GET    /languages/api/courses
POST   /languages/api/courses
PUT    /languages/api/courses/{id}
DELETE /languages/api/courses/{id}
```

---

### 4️⃣ Activity Management Service (8084)

**Responsibilities**: Activity planning and scheduling

**Features**:
- Course scheduling
- Room availability and management
- Calendar views

**Key Endpoints**:
```
GET    /activities/api/schedules
POST   /activities/api/schedules
GET    /activities/api/rooms/{id}/availability
```

---

### 5️⃣ Social Interaction Service (8085)

**Responsibilities**: Messaging and social communications

**Features**:
- 💬 Bidirectional messaging
- 🔍 Elasticsearch full-text search
- 📖 Read/unread tracking
- 🎯 Message filtering

**External Integration**: Elasticsearch

**Key Endpoints**:
```
GET    /socials/api/messages
POST   /socials/api/messages
GET    /socials/api/conversations/{id}/messages
PUT    /socials/api/messages/{id}/read
```

---

### 6️⃣ Community Engagement Service (8087)

**Responsibilities**: Events and community activities

**Features**:
- 🎉 Event creation and management
- 👥 User registration
- 📍 Capacity limits
- 📊 Status tracking

**Key Endpoints**:
```
GET    /community/api/events
POST   /community/api/events
GET    /community/api/events/{id}
POST   /community/api/events/{id}/register
DELETE /community/api/events/{id}/unregister/{userId}
```

---

### 🔌 API Gateway (8081)

**CORS Configuration**:
```
Allowed Origin:  http://localhost:4200
Allowed Methods: GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD
Allow Credentials: true
```

---

### 🔍 Discovery Service — Eureka (8761)

All services register automatically on startup. Dashboard available at: **http://localhost:8761**

```properties
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

---

## 🎨 Frontend

### Technologies

- **Framework**: Angular 18.2.0
- **Language**: TypeScript 5.5.2
- **Styling**: SCSS
- **Rendering**: SSR (Server-Side Rendering)
- **Real-time**: WebSocket (SockJS + STOMP)

### Feature Modules

| Module | Description |
|--------|-------------|
| **Courses** | Browse and manage academic courses |
| **Games** | Word battles (`WordBattleArena`), collaborative stories (`StoryChainArena`) |
| **Events** | Calendar and community event management |
| **Messaging** | Real-time chat interface |
| **Dashboard** | Home page and statistics |

---

## 🧪 Testing

### Backend — JUnit 5 + Mockito

```bash
# Run all tests
cd Back-PIDEV-4SAE7-2026-DEVUNITY
mvn test

# Specific service
mvn -pl Academic_Management_Service test

# Specific test class
mvn -Dtest=CourseServiceTest test

# Specific test method
mvn -Dtest=CourseServiceTest#testFindAllCourses test
```

**Main Test Suites**:

| Suite | Tests | Coverage |
|-------|-------|----------|
| `CourseServiceTest` | 10+ | Course CRUD, enrollment, quizzes, visibility |
| `EnrollmentServiceTest` | 9+ | Enrollment, duplicate validation, Feign client |
| `TestServiceTest` | 20+ | Sessions, timers, fraud detection, grading, certificates |
| `QuestionServiceTest` | 8+ | Quiz questions, authorization |
| `MessageServiceTest` | 8+ | Bidirectional messaging, read/unread |
| `EventServiceTest` | 7+ | Event lifecycle, capacity limits |

### Frontend — Jasmine + Karma

```bash
cd Front-PIDEV-4SAE7-2026-DEVUNITY

# Run all tests
ng test

# CI mode (headless)
ng test --watch=false --browsers=ChromeHeadless

# With code coverage
ng test --watch=false --code-coverage
# Report: coverage/DevUnity-FrontEnd/index.html
```

**Main Component Tests**:
- `EmployeeApplicationsComponent` — Application submission and filtering
- `WordBattleArenaComponent` — Real-time game logic (WebSocket)
- `StoryChainArenaComponent` — Collaborative story creation
- `StudentKanbanEventsComponent` — Task management with Drag & Drop

---

## 🚀 Deployment

### Docker Compose (Recommended)

Create a `docker-compose.yml`:

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  keycloak:
    image: quay.io/keycloak/keycloak:24.0
    environment:
      KEYCLOAK_ADMIN: ${KEYCLOAK_ADMIN_USER}
      KEYCLOAK_ADMIN_PASSWORD: ${KEYCLOAK_ADMIN_PASSWORD}
    ports:
      - "8080:8080"
    command: start-dev

  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.10.0
    environment:
      discovery.type: single-node
      xpack.security.enabled: "false"
    ports:
      - "9200:9200"

volumes:
  mysql_data:
```

Create a `.env` file:
```env
MYSQL_ROOT_PASSWORD=<your-secure-password>
KEYCLOAK_ADMIN_USER=admin
KEYCLOAK_ADMIN_PASSWORD=<keycloak-password>
```

Start:
```bash
docker-compose up -d
```

---

## 🔗 External Integrations

### Keycloak (Authentication)
- **URL**: http://localhost:8080
- **Realm**: `devunity` | **Client**: `devunity-app`
- **Protocol**: OAuth2 / OIDC
- **Realm config**: `Back-PIDEV-4SAE7-2026-DEVUNITY/keycloak-realm.json`

### Cloudinary (Image Storage)
Used by Academic Management Service.
```properties
cloudinary.api-key=${CLOUDINARY_API_KEY}
cloudinary.api-secret=${CLOUDINARY_API_SECRET}
cloudinary.cloud-name=${CLOUDINARY_CLOUD_NAME}
```

### Gmail SMTP (Email Notifications)
Used by Academic Management Service for enrollment notifications and certificates.
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${GMAIL_USERNAME}
spring.mail.password=${GMAIL_APP_PASSWORD}
```

### Elasticsearch (Full-text Search)
Used by Social Interaction Service.
```properties
spring.elasticsearch.uris=https://localhost:9200
spring.elasticsearch.username=${ELASTICSEARCH_USER}
spring.elasticsearch.password=${ELASTICSEARCH_PASSWORD}
spring.elasticsearch.ssl.verification-mode=none
```

---

## 🌍 Environment Variables Reference

All sensitive values must be set as environment variables — **never hardcode them**.

```bash
# Database
DB_USERNAME=root
DB_PASSWORD=<secure-password>

# Keycloak
KEYCLOAK_ADMIN_USER=admin
KEYCLOAK_ADMIN_PASSWORD=<keycloak-password>

# Cloudinary
CLOUDINARY_API_KEY=<api-key>
CLOUDINARY_API_SECRET=<api-secret>
CLOUDINARY_CLOUD_NAME=<cloud-name>

# Gmail
GMAIL_USERNAME=<email@gmail.com>
GMAIL_APP_PASSWORD=<app-specific-password>

# Elasticsearch
ELASTICSEARCH_USER=elastic
ELASTICSEARCH_PASSWORD=<elasticsearch-password>
```

---

## 🤝 Contributing

### Code Conventions

- **Backend**: Follow Spring Boot and microservices architecture guidelines
- **Frontend**: Follow Angular conventions and ESLint rules
- **Branches**: `feature/feature-name`, `bugfix/bug-name`
- **Commits**: Clear messages in English or French

### Pull Request Process

1. Branch off from `develop`
2. Implement the feature or fix
3. Run all tests and verify they pass
4. Open a PR with a detailed description

---

## 🐛 Troubleshooting

### Checklist

- [ ] MySQL is running: `mysql -u root -p`
- [ ] Keycloak accessible: http://localhost:8080
- [ ] Eureka dashboard: http://localhost:8761
- [ ] All ports available: 8081–8087
- [ ] Environment variables are set correctly
- [ ] Elasticsearch running (if using Social Service)

### Common Issues

**Services not appearing in Eureka**
- Verify `spring.application.name` is set correctly
- Ensure Eureka is running before starting other services
- Check network connectivity to port 8761

**CORS errors**
- Verify API Gateway CORS config matches your frontend origin
- Clear browser cache and retry

**Database connection errors**
- Check `DB_USERNAME` / `DB_PASSWORD` env vars
- Databases are auto-created by Hibernate if they don't exist
- Verify MySQL is listening on port 3306

---

## 📝 Additional Documentation

- **Testing details**: `TESTING_DOCUMENTATION.md` in each module folder
- **API docs**: Swagger UI available via API Gateway
- **Keycloak setup**: `keycloak-realm.json`

---

## 📄 License

Educational ESPRIT Project — All rights reserved

---

## ✉️ Contact

**DevUnity Team**  
ESPRIT — Higher School of Private Engineering and Technology

---

**Last Updated**: April 2026 | **Status**: Active Development — Sprint 2  
**Stack**: Spring Boot 3.2.5 · Spring Cloud 2023.0.1 · Angular 18.2.0 · MySQL 8.0 · Java 17
