# 📄 Astecob - Tax Guide Automation and Notification System

> A Java and Spring Boot application designed to automate tax guide PDF monitoring, data extraction, accounting client management, and multi-channel notification orchestration (Email and WhatsApp), featuring remote update checks and validated JAR downloads via GitHub Gist.

<p align="left">
  <img src="https://img.shields.io/badge/Java-21-red?style=for-the-badge" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen?style=for-the-badge" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/H2-File%20Database-blue?style=for-the-badge" alt="H2 File Database" />
  <img src="https://img.shields.io/badge/Spring%20Data%20JPA-Hibernate-purple?style=for-the-badge" alt="Spring Data JPA and Hibernate" />
  <img src="https://img.shields.io/badge/Flyway-Database%20Migrations-red?style=for-the-badge" alt="Flyway" />
  <img src="https://img.shields.io/badge/Apache%20PDFBox-PDF%20Processing-orange?style=for-the-badge" alt="Apache PDFBox" />
  <img src="https://img.shields.io/badge/JUnit-Testing-25A162?style=for-the-badge" alt="JUnit" />
  <img src="https://img.shields.io/badge/Thymeleaf-Web%20Dashboard-005F0F?style=for-the-badge" alt="Thymeleaf Web Dashboard" />
  <img src="https://img.shields.io/badge/Maven-Wrapper-orange?style=for-the-badge" alt="Maven" />
  <img src="https://img.shields.io/badge/Status-In%20Development-yellow?style=for-the-badge" alt="Status" />
</p>

---

## 📌 About the Project

**Astecob** is an automation system tailored for accounting workflows, designed to reduce manual effort in tax document distribution.

The system monitors local directories in real time, parses filenames for company and reference-period metadata, and reads PDF files using Apache PDFBox to extract due dates. It matches guides against normalized client records and triggers automated notifications via Email and WhatsApp.

An integrated **Web Dashboard**, powered by Thymeleaf, provides visual client management, notification history auditing, and manual email re-sends. A built-in **remote update mechanism** checks release metadata on GitHub Gist and downloads a replacement JAR after validating its archive structure and executable manifest. Installing the downloaded update remains a separate step.

## 🚀 Key Features

- 📁 **Automated Directory Monitoring:** Scans existing PDFs and watches for incoming tax guides in the configured directory.
- 📄 **PDF Reading & Data Extraction:** Extracts guide metadata from filenames and due dates from PDF text using Apache PDFBox.
- 👤 **Client Management:** Registers, lists, edits, and removes clients through the dashboard, with company-name normalization and REST endpoints for registration and lookup.
- ✉️ **Notification Orchestration:** Integrates Gmail SMTP and the WhatsApp API with email-first processing, failure handling, and notification history.
- 📊 **Administrative Dashboard:** Displays clients, notification histories, and registration pendencies, with actions for manual email re-sends.
- 🔄 **Remote Update Checks & Downloads:** Reads release metadata from GitHub Gist and validates JAR structure, manifest, and main-class presence before replacing the previous download.
- 🗃️ **Persistence & Migrations:** Uses file-mode H2 with PostgreSQL compatibility mode and versioned Flyway migrations.
- 🧪 **Automated Testing:** Includes more than 45 test cases covering domain rules, use cases, controllers, directory monitoring, persistence, and update validation.

## 🏗️ Architecture

The project follows a layered architecture inspired by Ports & Adapters (Hexagonal Architecture), keeping core domain models and business rules independent from Spring and JPA:

```text
PDF File / Web Dashboard / REST API
          │
          ├── Infrastructure Layer
          │   ├── Directory Watcher and PDFBox Reader
          │   ├── JPA Adapters
          │   └── Email and WhatsApp Clients
          │
          ├── Application Layer
          │   ├── Use Cases and Ports
          │   ├── Notification Orchestrator
          │   └── Update Service
          │
          ├── Domain Layer
          │   ├── Models and Enums
          │   ├── Domain Services
          │   ├── Repository Contracts
          │   └── Business Rules
          │
          └── H2 Database (File Mode / PostgreSQL Compatibility)
```

## 📦 Project Structure

```text
src/
├── main/
│   ├── java/br/com/astecob/aviso_guias/
│   │   ├── application/
│   │   │   ├── port/
│   │   │   ├── service/
│   │   │   └── usecase/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── domain/
│   │   │   ├── enums/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   ├── dto/
│   │   ├── exception/
│   │   └── infrastructure/
│   │       ├── email/
│   │       ├── notificacao/
│   │       ├── pdf/
│   │       ├── persistence/
│   │       ├── watcher/
│   │       ├── web/
│   │       └── whatsapp/
│   └── resources/
│       ├── db/migration/
│       ├── templates/
│       ├── application.properties
│       ├── application-dev.properties
│       └── application-prod.properties
└── test/
    └── java/br/com/astecob/aviso_guias/
```

## 🔗 Endpoints and Dashboard Routes

### Client REST API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/clientes` | Register a new client |
| GET | `/clientes` | List all registered clients |
| GET | `/clientes/{id}` | Find a client by ID |

### Web Panel (Dashboard)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/dashboard` | Main panel with clients, notification history, and registration pendencies |
| POST | `/dashboard/clientes` | Register a client through the web UI |
| POST | `/dashboard/clientes/{id}/editar` | Update client registration details |
| POST | `/dashboard/clientes/{id}/deletar` | Remove a client |
| POST | `/dashboard/guias/{id}/reenviar` | Request an email re-send for a specific guide |

## 📄 Guide File Naming Convention

PDF files placed inside the watched directory must follow this naming structure:

```text
GUIDE_TYPE-COMPANY_NAME-MONTH-YEAR.pdf
```

Example:

```text
DAS-Example Company LLC-06-2026.pdf
```

The PDF text must contain the due-date label used by the extractor:

```text
Pagar este documento até DD/MM/YYYY
```

## 🗄️ Database & Migrations

The application uses a local H2 file database at `./data/aviso_guias_db`, configured with PostgreSQL compatibility mode. Docker is not required for the default configuration.

Flyway manages the schema through versioned migrations in `src/main/resources/db/migration/`:

- `V1__cria_tabela_clientes.sql` — clients table
- `V2__cria_tabelas_notificacao.sql` — guides, notification history, pendencies, and accountant notices
- `V3__adicionar_colunas_cliente_historico.sql` — company and contact metadata in notification history

The repository also includes a PostgreSQL Docker Compose configuration. `DB_PASSWORD` is used by that container; the default H2 configuration does not read this variable.

## ▶️ How to Run

### Prerequisites

- Java 21+ with `JAVA_HOME` configured
- Maven Wrapper (included in the repository)
- PowerShell or Bash
- Gmail SMTP and Meta WhatsApp credentials, plus internet access for notifications and update checks

### Environment Configuration

Store integration credentials in a local `.env` file if desired. This file is ignored by Git and must never be committed:

```env
DB_PASSWORD=your_local_password
GMAIL_APP_PASSWORD=your_gmail_app_password
WHATSAPP_TOKEN=your_meta_whatsapp_token
WHATSAPP_PHONE_NUMBER_ID=your_phone_number_id
```

The application does **not** automatically load `.env`. Configure your IDE to load it, or set the variables in the terminal before starting the application:

```powershell
$env:GMAIL_APP_PASSWORD="your_gmail_app_password"
$env:WHATSAPP_TOKEN="your_meta_whatsapp_token"
$env:WHATSAPP_PHONE_NUMBER_ID="your_phone_number_id"
```

On Bash:

```bash
export GMAIL_APP_PASSWORD="your_gmail_app_password"
export WHATSAPP_TOKEN="your_meta_whatsapp_token"
export WHATSAPP_PHONE_NUMBER_ID="your_phone_number_id"
```

The default profile is `prod`. Its SMTP username and sender address are configured for the application's Gmail account; the app password must belong to that account.

### Run the Application

On Windows:

```powershell
./mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

Open the dashboard at **http://localhost:8080/dashboard**.

Configure the watched directory using `astecob.guias.diretorio`. The production profile sets it to `C:/Astecob/guias`; adjust it for your machine. Processed and rejected files use `app.diretorio.processados` and `app.diretorio.erros` respectively.

### Run Tests

```powershell
./mvnw.cmd test
```

On Linux or macOS, use `./mvnw test`. Tests that load the application context use its configured profiles, credentials, and database unless explicitly overridden. Use a separate test environment for the full suite; the `dev` profile alone does not disable the real WhatsApp client or startup update checks.

### Build the Application

```powershell
./mvnw.cmd clean package -DskipTests
```

This compiles and packages the application without running tests. The executable JAR is generated in `target/`.

## 🧠 Development Approach

```text
Understand the problem → Model the domain → Implement one block → Test → Refactor → Evolve
```

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 4.1.0** — Data JPA, Web, Validation, and Mail
- **Thymeleaf** — dashboard UI
- **H2 Database** — file storage with PostgreSQL compatibility mode
- **Flyway** — database migrations
- **Apache PDFBox** — PDF processing
- **Jackson** — JSON handling for update metadata
- **JUnit & Mockito** — automated testing
- **Maven Wrapper** — build and dependency management
- **Docker Compose / PostgreSQL 16** — optional database setup

## 👨‍💻 Author

**Vinicius Pereira**

Backend Developer — Java & Spring Boot
