# 🌿 Jungle In English — E-Learning Platform

![Status](https://img.shields.io/badge/Status-Active-success?style=for-the-badge)
![Angular](https://img.shields.io/badge/Angular_18-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.2-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Python](https://img.shields.io/badge/Python_3.9-3670A0?style=for-the-badge&logo=python&logoColor=ffdd54)
![MySQL](https://img.shields.io/badge/MySQL_8-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Keycloak](https://img.shields.io/badge/Keycloak-4D4D4D?style=for-the-badge&logo=keycloak&logoColor=white)

> **Jungle In English** is a microservices-based e-learning platform that bridges academic learning and professional recruitment. It uses AI/ML for dropout prediction, automated CV screening, and personalized learning paths.

---

## 📋 Table of Contents

- [Description](#-description)
- [Technologies](#️-technologies)
- [AI Features](#-ai-features)
- [Prerequisites](#-prerequisites)
- [Installation — Step by Step](#-installation--step-by-step)
- [Verify Everything is Running](#-verify-everything-is-running)
- [API Documentation](#-api-documentation)
- [Demo](#-demo)
- [Authors](#-authors)

---

## 📝 Description

Jungle In English is a full-stack microservices platform built for ESPRIT School of Engineering. It provides:
- A complete e-learning environment with courses, certifications, and quizzes
- AI-powered student dropout prediction and CV analysis
- Community management (clubs, events, social interactions)
- Language courses with CEFR level evaluation (A1 → C2)
- Recruitment and activity tracking for students and employers

---

## 🛠️ Technologies

| Layer | Technologies |
|:---|:---|
| **Frontend** | Angular 18, Tailwind CSS, RxJS |
| **Backend** | Spring Boot 3.2, Spring Cloud (Gateway, Eureka, OpenFeign) |
| **Security** | Keycloak (OAuth2 / OIDC / JWT) |
| **AI / ML** | Python 3.9, FastAPI, Scikit-learn, NLP |
| **Database** | MySQL 8 (one schema per microservice) |
| **Media** | Cloudinary |
| **Messaging** | WebSockets (StompJS), Spring Mail (SMTP) |

---

## 🤖 AI Features

| Feature | Description | Model Performance |
|:---|:---|:---|
| 📉 **Dropout Prediction** | Predicts students at risk of dropping out | Accuracy: 87%, F1: 0.85 |
| 📄 **CV Analysis** | Automated scoring and clustering of candidates | Precision: 82% |
| 🇬🇧 **CEFR Level Testing** | Evaluates English proficiency (A1 → C2) | Accuracy: 90% |
| 🎓 **Course Recommender** | Personalized learning paths based on student profile | Accuracy: 84% |

> 📦 **Trained models** (`.joblib` files) are hosted externally — download from [OneDrive](https://1drv.ms/f/c/0dd392f1aa339042/IgBEhJhDDcvRQLLNkDCoBVoKAXbew53iWzMJD_lN-9tZ8fQ?e=Hyzrfd) and place in `ML_Dropout_Service/models/`.

### 📊 Datasets

The following datasets were used to train the ML models:

| Dataset | Description | Source |
|:---|:---|:---|
| **Student Dropout Analysis & Prediction** | Labeled student data used to train the dropout prediction and churn models | [Kaggle](https://www.kaggle.com/datasets/abdullah0a/student-dropout-analysis-and-prediction-dataset) |
| **Project Datasets (OneDrive)** | Additional datasets used for training CV analysis and course recommender models | [OneDrive](https://1drv.ms/f/c/0dd392f1aa339042/IgC4L1Q68SYhTZSfo-BJ8ZfHAfcrcV6wprE_Zvm1wf4cZ7E?e=LErzVs) |

---

## ✅ Prerequisites

Install the following tools before starting:

| Tool | Version | Download |
|:---|:---|:---|
| **Java JDK** | 17+ | https://adoptium.net |
| **Maven** | 3.8+ | https://maven.apache.org/download.cgi |
| **Node.js** | 18+ | https://nodejs.org |
| **Angular CLI** | 17+ | `npm install -g @angular/cli` |
| **Python** | 3.9+ | https://www.python.org/downloads |
| **MySQL** | 8.0+ | https://dev.mysql.com/downloads |
| **Keycloak** | 22+ | https://www.keycloak.org/downloads |
| **Git** | latest | https://git-scm.com |

Verify your installations:

```bash
java -version        # should show 17+
mvn -version         # should show 3.8+
node -v              # should show 18+
python --version     # should show 3.9+
mysql --version      # should show 8.0+
```

---

## 🚀 Installation — Step by Step

### Step 1 — Clone the repository

```bash
git clone https://github.com/BouthaynaHammami/Esprit-PISAE-4SAE7-2526-JungleInEnglish.git
cd Esprit-PIDEV-4SAE7-2026-DEVUNITY
```

---

### Step 2 — Configure environment variables with EnvFile plugin

This project uses the **EnvFile** plugin for IntelliJ IDEA to load environment variables from `.env.example` directly into your run configurations.

#### 2.1 — Install the EnvFile plugin

1. Open **IntelliJ IDEA**
2. Go to **File → Settings → Plugins** (or `Ctrl+Alt+S` → Plugins)
3. Search for **EnvFile**
4. Click **Install** then **Restart IDE**

#### 2.2 — Edit `.env.example` with your own values

> ⚠️ Before copying, open `.env.example` and replace all placeholder values with your own credentials:

```env
DB_PASSWORD=your_mysql_password        # ← replace with your MySQL password
KEYCLOAK_ADMIN_PASSWORD=your_password  # ← replace with your Keycloak admin password
MAIL_USERNAME_LANGUAGE=your@gmail.com  # ← replace with your Gmail address
MAIL_PASSWORD_LANGUAGE=your_app_pass   # ← replace with your Gmail App Password
CLOUDINARY_API_KEY=your_key            # ← replace with your Cloudinary key
GEMINI_API_KEY=your_key                # ← replace with your Gemini API key
# ... fill in all fields marked with your_*
```

Then copy it to `.env`:

```bash
# Windows
copy .env.example .env

# macOS / Linux
cp .env.example .env
```

#### 2.3 — Import `.env` in IntelliJ Run Configuration

1. Open **Run → Edit Configurations...**
2. Select your Spring Boot service (e.g. `AcademicManagementApplication`)
3. Click the **EnvFile** tab
4. Check ✅ **Enable EnvFile**
5. Click **+** → **Add** → select your `.env` file at the root of the project
6. Click **OK** / **Apply**
7. Repeat for each microservice run configuration

> 💡 **Tip:** You can also set the `.env` path as `$PROJECT_DIR$/.env` to make it portable across machines.

---

### Step 3 — Set up MySQL databases

Open MySQL and run the following commands — the databases will be created automatically by Spring Boot on first launch, but you can create them manually to be sure:

```sql
CREATE DATABASE IF NOT EXISTS Academic_Management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS Activity_Management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS Community_Engagement CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS Language_Courses    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS Learner_Management  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS Social_Interaction  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```


#### 3.1 — Download & import the pre-filled databases (optional but recommended)

> 📦 **Download link:** [devunity-databases — OneDrive](https://1drv.ms/f/c/0dd392f1aa339042/IgB4p49fnJMeSoU8aiQtnzXQAZaUppFh9a_Oq7SX_Vekq2Q?e=YnAjay)

Download the zip, extract it — you will get one `.sql` dump file per database.

**Option A — Via MySQL Workbench**

1. Open **MySQL Workbench** and connect to your local server
2. Go to **Server → Data Import**
3. Select **Import from Self-Contained File** and browse to the `.sql` file
4. Under **Default Schema to be Imported To**, select the matching database (e.g. `Academic_Management`)
5. Click **Start Import**
6. Repeat for each `.sql` file

**Option B — Via terminal**

```bash
# Replace <password> with your MySQL password
# Repeat for each dump file

mysql -u root -p<password> Academic_Management  < Academic_Management.sql
mysql -u root -p<password> Activity_Management  < Activity_Management.sql
mysql -u root -p<password> Community_Engagement < Community_Engagement.sql
mysql -u root -p<password> Language_Courses     < Language_Courses.sql
mysql -u root -p<password> Learner_Management   < Learner_Management.sql
mysql -u root -p<password> Social_Interaction   < Social_Interaction.sql
```

> ⚠️ Make sure the databases are created (step above) before importing.

---

### Step 4 — Configure and start Keycloak

**4.1 — Start Keycloak:**

```bash
# Windows — go to your Keycloak installation folder
cd C:\keycloak\bin
kc.bat start-dev --http-port=8080

# macOS / Linux
cd ~/keycloak/bin
./kc.sh start-dev --http-port=8080
```

**4.2 — Access the admin console:**

Open http://localhost:8080 → log in with your admin credentials.

**4.3 — Import the realm (recommended)**

> 💡 A ready-to-use realm file `keycloak-realm.json` is included in the project root. It automatically creates the realm `devunity`, the client `devunity-app`, and all roles (`ADMIN`, `STUDENT`, `TUTOR`, `EMPLOYE`, `COMPANY`).

1. In the Keycloak admin console, click **Create Realm**
2. Click **Browse...** and select the file `keycloak-realm.json` from the project root
3. Click **Create** — the realm is imported with all settings pre-configured

✅ The following are automatically created upon import:

| Element | Value |
|:---|:---|
| Realm | `devunity` |
| Client | `devunity-app` (Public) |
| Redirect URIs | `http://localhost:4200/*`, `http://localhost:8081/*` |
| Roles | `ADMIN`, `STUDENT`, `TUTOR`, `EMPLOYE`, `COMPANY` |

**4.4 — Create a test user:**

Realm → **Users** → **Add user** → set username and email → **Credentials** tab → set a password → **Role mapping** → assign a role.

---

### Step 5 — Start the Discovery Service (Eureka)

> ⚠️ Start this first — all other services register here.

```bash
cd Back-PIDEV-4SAE7-2026-DEVUNITY/Discovery_Service
mvn spring-boot:run
```

Wait until you see in the console:
```
Started DiscoveryServiceApplication on port 8761
```

✅ Verify: open http://localhost:8761 → Eureka dashboard should appear.

---

### Step 6 — Start the API Gateway

> ⚠️ Start this second — before any business service.

```bash
# Open a new terminal
cd Back-PIDEV-4SAE7-2026-DEVUNITY/API_Gateway
mvn spring-boot:run
```

Wait for:
```
Started ApiGatewayApplication on port 8081
```

---

### Step 7 — Start all backend microservices

Open a **separate terminal for each service** and run:

**Academic Management Service (port 8086)**
```bash
cd Back-PIDEV-4SAE7-2026-DEVUNITY/Academic_Management_Service
mvn spring-boot:run
```

**Activity Management Service (port 8083)**
```bash
cd Back-PIDEV-4SAE7-2026-DEVUNITY/Activity_Management_Service
mvn spring-boot:run
```

**Community Engagement Service (port 8084)**
```bash
cd Back-PIDEV-4SAE7-2026-DEVUNITY/Community_Engagement_Service
mvn spring-boot:run
```

**Language Courses Service (port 8087)**
```bash
cd Back-PIDEV-4SAE7-2026-DEVUNITY/Language_Courses_Service
mvn spring-boot:run
```

**Learner Management Service (port 8082)**
```bash
cd Back-PIDEV-4SAE7-2026-DEVUNITY/Learner_Management_Service
mvn spring-boot:run
```

**Social Interaction Service (port 8085)**
```bash
cd Back-PIDEV-4SAE7-2026-DEVUNITY/Social_Interaction_Service
mvn spring-boot:run
```

> 💡 **Tip for Windows:** Create `start-all.bat` at the root to launch everything at once:

```bat
@echo off
start cmd /k "cd Back-PIDEV-4SAE7-2026-DEVUNITY\Discovery_Service && mvn spring-boot:run"
timeout /t 20
start cmd /k "cd Back-PIDEV-4SAE7-2026-DEVUNITY\API_Gateway && mvn spring-boot:run"
timeout /t 10
start cmd /k "cd Back-PIDEV-4SAE7-2026-DEVUNITY\Academic_Management_Service && mvn spring-boot:run"
start cmd /k "cd Back-PIDEV-4SAE7-2026-DEVUNITY\Activity_Management_Service && mvn spring-boot:run"
start cmd /k "cd Back-PIDEV-4SAE7-2026-DEVUNITY\Community_Engagement_Service && mvn spring-boot:run"
start cmd /k "cd Back-PIDEV-4SAE7-2026-DEVUNITY\Language_Courses_Service && mvn spring-boot:run"
start cmd /k "cd Back-PIDEV-4SAE7-2026-DEVUNITY\Learner_Management_Service && mvn spring-boot:run"
start cmd /k "cd Back-PIDEV-4SAE7-2026-DEVUNITY\Social_Interaction_Service && mvn spring-boot:run"
```

> 💡 **Tip for macOS/Linux:** Create `start-all.sh`:

```bash
#!/bin/bash
cd Back-PIDEV-4SAE7-2026-DEVUNITY/Discovery_Service && mvn spring-boot:run &
sleep 20
cd ../../Back-PIDEV-4SAE7-2026-DEVUNITY/API_Gateway && mvn spring-boot:run &
sleep 10
cd ../../Back-PIDEV-4SAE7-2026-DEVUNITY/Academic_Management_Service && mvn spring-boot:run &
cd ../../Back-PIDEV-4SAE7-2026-DEVUNITY/Activity_Management_Service && mvn spring-boot:run &
cd ../../Back-PIDEV-4SAE7-2026-DEVUNITY/Community_Engagement_Service && mvn spring-boot:run &
cd ../../Back-PIDEV-4SAE7-2026-DEVUNITY/Language_Courses_Service && mvn spring-boot:run &
cd ../../Back-PIDEV-4SAE7-2026-DEVUNITY/Learner_Management_Service && mvn spring-boot:run &
cd ../../Back-PIDEV-4SAE7-2026-DEVUNITY/Social_Interaction_Service && mvn spring-boot:run &
```

---

### Step 8 — Start the ML Service (FastAPI)

#### 8.0 — Download the ML models

> ⚠️ The trained model files are **not included in the repository** (too large for Git). You must download them manually.

📦 **Download link:** [ML Models — OneDrive](https://1drv.ms/f/c/0dd392f1aa339042/IgBEhJhDDcvRQLLNkDCoBVoKAXbew53iWzMJD_lN-9tZ8fQ?e=Hyzrfd)

After downloading, place all `.joblib` files inside the `models/` folder:

```
ML_Dropout_Service/
└── models/
    ├── best_classification_model.joblib
    ├── best_regression_model.joblib
    ├── churn_prediction.joblib
    └── resume.joblib
```

```bash
cd Back-PIDEV-4SAE7-2026-DEVUNITY/ML_Dropout_Service

# Create virtual environment (recommended)
python -m venv venv

# Activate it
# Windows:
venv\Scripts\activate
# macOS / Linux:
source venv/bin/activate

# Install dependencies
pip install -r requirements.txt

# Start the service
python app/main.py
```

Wait for:
```
INFO:     Uvicorn running on http://0.0.0.0:8090
```

✅ Verify: open http://localhost:8090/docs

---

### Step 9 — Start the Frontend (Angular)

```bash
cd Front-PIDEV-4SAE7-2026-DEVUNITY

# Install dependencies
npm install

# Start the app
npm start
```

Wait for:
```
✔ Compiled successfully.
Local: http://localhost:4200/
```

✅ Open http://localhost:4200 in your browser.

---

## ✅ Verify Everything is Running

Open these URLs to confirm all services are up:

| Service | URL | Expected |
|:---|:---|:---|
| Eureka Dashboard | http://localhost:8761 | |
| API Gateway | http://localhost:8081 | |
| Academic Service | http://localhost:8086 | |
| Activity Service | http://localhost:8083 | |
| Community Service | http://localhost:8084 | |
| Language Service | http://localhost:8087 | |
| Learner Service | http://localhost:8082 | |
| Social Service | http://localhost:8085 | |
| ML Service | http://localhost:8090/docs | |
| Frontend | http://localhost:4200 | |
| Keycloak | http://localhost:8080 | |

---

## 📑 API Documentation

Full API documentation is available in [`docs/api.md`](docs/api.md).

| Service | URL |
|:---|:---|
| Academic | http://localhost:8086 |
| Activity | http://localhost:8083 |
| Community | http://localhost:8084 |
| Language | http://localhost:8087 |
| Learner | http://localhost:8082 |
| Social | http://localhost:8085 |
| ML Service | http://localhost:8090 |
| API Gateway | http://localhost:8081 |

---

## 🎬 Demo

- 📹 **Video demo:** [Watch on OneDrive](https://1drv.ms/v/c/0dd392f1aa339042/IQCB-0jhhXxSR6lQKpL3CMuuATolULCraYdb-1OUmsPGymw?e=SipUa1)

### 📸 Screenshots

<table>
  <tr>
    <td><img src="demo/screenshots/screenshot-1.png" alt="Screenshot 1" width="280"/></td>
    <td><img src="demo/screenshots/screenshot-2.png" alt="Screenshot 2" width="280"/></td>
    <td><img src="demo/screenshots/screenshot-3.png" alt="Screenshot 3" width="280"/></td>
  </tr>
  <tr>
    <td><img src="demo/screenshots/screenshot-4.png" alt="Screenshot 4" width="280"/></td>
    <td><img src="demo/screenshots/screenshot-5.png" alt="Screenshot 5" width="280"/></td>
    <td><img src="demo/screenshots/screenshot-6.png" alt="Screenshot 6" width="280"/></td>
  </tr>
</table>

---

## ❓ Troubleshooting

**Services not registering in Eureka**
→ Make sure Discovery Service started first and is running on port 8761 before launching other services.

**MySQL connection refused**
→ Verify MySQL is running: `net start mysql` (Windows) or `sudo service mysql start` (Linux).

**Keycloak token errors**
→ Check that the realm name is exactly `devunity` and the client IDs match `.env`.

**Angular npm install fails**
→ Try `npm install --legacy-peer-deps` or delete `node_modules/` and retry.

**Python venv not found**
→ Use `python3 -m venv venv` on macOS/Linux.

**ML model file not found**
→ Download the model files from [this OneDrive link](https://1drv.ms/f/c/0dd392f1aa339042/IgBEhJhDDcvRQLLNkDCoBVoKAXbew53iWzMJD_lN-9tZ8fQ?e=Hyzrfd) and place them in `ML_Dropout_Service/models/`.

---

## 👥 Authors

| Name | Class | Role | Year |
|:---|:---|:---|:---|
| Saif Elislam Ben Youssef | 4SAE7 | Full Stack Developer | 2025–2026 |
| Bouthayna Hammami | 4SAE7 | Full Stack Developer | 2025–2026 |
| Islem Raissi | 4SAE7 | Full Stack Developer | 2025–2026 |
| Marwa Chaibi | 4SAE7 | Full Stack Developer | 2025–2026 |
| Shayma Tlili | 4SAE7 | Full Stack Developer | 2025–2026 |
| Hibatallah Jlassi | 4SAE7 | Full Stack Developer | 2025–2026 |

**Tutor:** Mr. Ala Rami
**Institution:** ESPRIT School of Engineering — Honoris United Universities

---

## 📝 License

Distributed under the **MIT License**. See [`LICENSE`](LICENSE) for more information.

---

<div align="center">
  Built with ❤️ by the <strong>Jungle In English Team</strong> — ESPRIT 4SAE7 — 2026
</div>
