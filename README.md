# 📄 Tax Guide Notification System

> A Java and Spring Boot application that monitors tax-guide PDFs, extracts their information, manages accounting clients, and provides the foundation for automated notifications and notification history.

<p align="left">
  <img src="https://img.shields.io/badge/Java-21-red?style=for-the-badge" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen?style=for-the-badge" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Spring%20Data%20JPA-Hibernate-purple?style=for-the-badge" alt="Spring Data JPA and Hibernate" />
  <img src="https://img.shields.io/badge/Flyway-Database%20Migrations-red?style=for-the-badge" alt="Flyway" />
  <img src="https://img.shields.io/badge/Apache%20PDFBox-PDF%20Processing-orange?style=for-the-badge" alt="Apache PDFBox" />
  <img src="https://img.shields.io/badge/JUnit-Testing-25A162?style=for-the-badge" alt="JUnit" />
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge" alt="Docker Compose" />
  <img src="https://img.shields.io/badge/Maven-Wrapper-orange?style=for-the-badge" alt="Maven" />
  <img src="https://img.shields.io/badge/Status-In%20Development-yellow?style=for-the-badge" alt="Status" />
</p>

---

## 📌 About the Project

This project is an automation system created to help accounting offices process and notify clients about their tax guides.

The application watches a configured directory, identifies new PDF files, extracts the company, guide type, reference period, year, and due date, and prepares this information for the notification workflow.

The project is being developed incrementally through small implementation phases, with a focus on clean architecture, explicit domain rules, automated tests, and persistence of important business history.

## 🚀 Features

### Implemented

- 📁 Automatic monitoring of the configured guides directory
- 📄 Processing of existing and newly created PDF files
- 🔍 Guide filename parsing with type, company, month, and year
- 📅 Due-date extraction from PDF text
- 👤 Client registration, listing, and lookup by ID
- ✨ Company-name normalization to prevent duplicate clients
- ✅ Request validation and standardized application errors
- 🗃️ PostgreSQL persistence with Flyway migrations
- 🧩 Domain models for guides, notification history, client-registration pendencies, and accountant notices
- 🏗️ Repository contracts with JPA adapters
- 🧪 Unit, controller, watcher, and persistence tests

### Planned

- 📧 Email and WhatsApp notification ports and implementations
- 📝 Notification orchestration with email-first business rules
- ⚠️ Accountant alerts for failed or incomplete notifications
- ♻️ Persistent guide deduplication and complete processing workflow
- 🔗 Integration between the directory watcher and the notification use case
- 📊 Query endpoints for guides, notification history, and pendencies

## 🏗️ Architecture

The project follows a layered architecture inspired by Hexagonal Architecture, keeping business rules independent from Spring and JPA whenever possible:

```text
PDF File / HTTP Request
          │
          ├── Infrastructure Layer
          │   ├── Directory Watcher
          │   ├── PDF Reader
          │   └── JPA Adapters
          │
          ├── Application Layer
          │   ├── Use Cases
          │   └── Ports
          │
          ├── Domain Layer
          │   ├── Models
          │   ├── Services
          │   ├── Repository Contracts
          │   └── Business Rules
          │
          └── PostgreSQL Database
```

## 📦 Project Structure

```text
src/
└── main/
    ├── java/<base-package>/aviso_guias/
    │   ├── application/
    │   │   ├── port/
    │   │   └── usecase/
    │   ├── config/
    │   ├── controller/
    │   ├── domain/
    │   │   ├── enums/
    │   │   ├── model/
    │   │   ├── repository/
    │   │   └── service/
    │   ├── dto/
    │   ├── exception/
    │   └── infrastructure/
    │       ├── pdf/
    │       ├── persistence/
    │       └── watcher/
    └── resources/
        ├── db/migration/
        └── application.properties
```

## 🔗 Current API Endpoints

### Clients

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/clientes` | Register a client |
| GET | `/clientes` | List all clients |
| GET | `/clientes/{id}` | Find a client by ID |

## 📄 Guide File Convention

Guide files must use the following naming pattern:

```text
TIPO_GUIA-NOME_DA_EMPRESA-MES-ANO.pdf
```

Example:

```text
DAS-Example Company-06-2026.pdf
```

The PDF text must contain the due-date label used by the extractor:

```text
Pagar este documento até DD/MM/YYYY
```

## 🗄️ Database

Flyway manages the database schema through versioned migrations:

- `V1__cria_tabela_clientes.sql` — clients table
- `V2__cria_tabelas_notificacao.sql` — guides, notification history, pendencies, and accountant notices

The PostgreSQL container is exposed on port `5433` to avoid conflicts with a local PostgreSQL installation.

## ▶️ How to Run

### Prerequisites

- Java 21+
- Docker Desktop, or PostgreSQL 16+
- PowerShell, Bash, or another terminal

### Start PostgreSQL with Docker Compose

Set a local database password and start the database. Do not commit or publish the real value:

```powershell
$env:DB_PASSWORD="your-local-database-password"
docker compose up -d
```

The application expects the following database connection by default:

```text
jdbc:postgresql://127.0.0.1:5433/aviso_guias
```

### Run the application

```powershell
./mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

The guides directory is created automatically at `./guias`.

You can change the watched directory through the guide-directory property in `application.properties`.

### Run tests

```powershell
./mvnw.cmd test
```

Persistence tests require PostgreSQL to be running on port `5433`.

### Stop PostgreSQL

```powershell
docker compose down
```

## 🗺️ Development Roadmap

### Phase 1 — Project Foundation ✅

- [x] Spring Boot project setup
- [x] PostgreSQL and Flyway configuration
- [x] Basic application structure

### Phase 2 — Client Registration and Guide Reading ✅

- [x] Client domain model and persistence
- [x] Client REST endpoints
- [x] Company-name normalization
- [x] PDF reader using Apache PDFBox
- [x] Guide filename parser
- [x] Due-date extractor
- [x] Directory watcher

### Phase 3 — Notifications and History 🚧

- [x] Notification-related database migration
- [x] Guide, history, pendency, and accountant-notice domain models
- [x] Repository contracts and JPA adapters
- [ ] Email and WhatsApp ports and mock implementations
- [ ] Notification orchestration
- [ ] Persistent guide deduplication in the complete processing flow
- [ ] Connect notification processing to the directory watcher

## 🧠 Development Approach

```text
Understand the problem → Model the domain → Implement one block → Test → Refactor → Evolve
```

## 🛠️ Technologies

- Java 21
- Spring Boot 4.1.0
- Spring Data JPA
- Spring Validation
- PostgreSQL 16
- Flyway
- Apache PDFBox
- Maven Wrapper
- JUnit and Spring Boot Test
- Docker Compose

## 👨‍💻 Author

**Vinicius Pereira**

Backend Developer — Java & Spring Boot
