# 🏦 SWIFT Code Management API

A Spring Boot REST API for parsing, storing, and managing SWIFT code data — complete with Excel file import, full CRUD functionality, and PostgreSQL integration. Built as part of a 2025 internship project.

---

## 🚀 Features

- ✅ Import and parse SWIFT codes from Excel (.xlsx)
- ✅ Store data in PostgreSQL (headquarters + branches)
- ✅ Full REST API:
  - `GET` by SWIFT code
  - `GET` all codes by country
  - `POST` new code
  - `DELETE` by code
- ✅ Error handling
- ✅ Unit + Integration tests with Testcontainers
- ✅ Fully containerized with Docker & Docker Compose

---

## 📦 Technologies Used

| Layer       | Tech                     |
|-------------|--------------------------|
| Backend     | Java 17, Spring Boot     |
| Build Tool  | Maven                    |
| Persistence | PostgreSQL + JPA (Hibernate) |
| Tests       | JUnit 5, MockMvc, Testcontainers |
| Others      | Docker, Docker Compose   |

---

##  Getting Started

### 🔧 Requirements

- Java 17
- Docker + Docker Compose
- Git

---

## 🐳 Run with Docker 
# Clone the project
git clone https://github.com/krawiec00/RemitlyHomeExercise

Go to localization of the project

# Start the app and database
User command: docker-compose up --build

 API will be available at:
 http://localhost:8080


🔍 Example Endpoints
GET /v1/swift-codes/{swiftCode}

Fetch data for a specific code, including branches (if HQ).
GET /v1/swift-codes/country/{countryISO2}

Get all codes for a given country.
POST /v1/swift-codes

Create a new SWIFT code (JSON body required).
DELETE /v1/swift-codes/{swiftCode}

Remove a specific SWIFT code.
POST /v1/swift-codes/upload

Upload Excel .xlsx file and parse + save valid records.

🧪 Tests

Use command:

./mvnw test  

Tests include:

-  Unit tests for DTOs, mappers, services

-  Integration tests for REST endpoints

-  Excel import validation (invalid format, duplicates, etc.)

-  Run on real PostgreSQL DB via Testcontainers



📁 Environment Variables
Variable	Default

SPRING_DATASOURCE_URL	jdbc:postgresql://localhost:5432/swift_db

SPRING_DATASOURCE_USERNAME	swift_user

SPRING_DATASOURCE_PASSWORD	swift_pass

These can be overridden in docker-compose.yml.

✍️ Author
Karol Krawczyk


