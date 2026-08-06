# LostLink — Campus Lost & Found Management System.

A production-grade, microservices-based campus lost-and-found platform built with **Spring Boot 3**, **Spring Cloud**, and **React**. Designed for Organisations to digitize the traditional notice-board process.

---

## Architecture Overview

```
┌──────────────┐     ┌──────────────────┐     ┌────────────────────┐
│   React UI   │────►│  API Gateway     │────►│  LostLink Service  │
│   (Port 3000)│     │  (Port 8080)     │     │  (Port 8081)       │
└──────────────┘     └────────┬─────────┘     └────────┬───────────┘
                              │                        │
                    ┌─────────▼─────────┐              │
                    │  Eureka Server    │◄─────────────┤
                    │  (Port 8761)      │              │
                    └───────────────────┘              │
                    ┌───────────────────┐              │
                    │  Config Server    │◄─────────────┘
                    │  (Port 8888)      │
                    └───────────────────┘
                              │
                    ┌─────────▼─────────┐
                    │  MySQL Database   │
                    │  (Port 3306)      │
                    └───────────────────┘
```

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| **Backend** | Java 21, Spring Boot 3.2, Spring MVC |
| **Security** | Spring Security, JWT (JJWT 0.12), BCrypt |
| **Data** | Spring Data JPA, Hibernate, MySQL 8 |
| **Microservices** | Spring Cloud, Eureka, API Gateway, Config Server, OpenFeign |
| **Resilience** | Resilience4j (Circuit Breaker) |
| **API Docs** | SpringDoc OpenAPI 3.0 (Swagger UI) |
| **Frontend** | React 18, Vite, Tailwind CSS 3, Recharts, Lucide Icons |
| **DevOps** | Docker, Docker Compose, Multi-stage Builds |
| **Build** | Maven |

---

## Project Structure

```
LostLink/
├── pom.xml                          # Parent POM (dependency management)
├── docker-compose.yml               # One-command deployment
├── .gitignore
├── README.md
│
├── lostlink-service/                # Core business microservice
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/
│       ├── java/com/example/lostlink/
│       │   ├── LostLinkApplication.java
│       │   ├── controller/          # REST API endpoints
│       │   │   ├── AuthController.java
│       │   │   ├── LostItemController.java
│       │   │   ├── ClaimController.java
│       │   │   ├── DashboardController.java
│       │   │   ├── NotificationController.java
│       │   │   └── AdminController.java
│       │   ├── service/             # Business logic interfaces
│       │   ├── service/impl/        # Business logic implementations
│       │   ├── repository/          # Spring Data JPA repositories
│       │   ├── entity/              # JPA entities (User, LostItem, Claim, Notification)
│       │   ├── dto/                 # Data Transfer Objects
│       │   ├── config/              # Security, Swagger, Feign, Resilience configs
│       │   ├── security/            # JWT filter, token provider, UserDetailsService
│       │   ├── exception/           # Custom exceptions + global handler
│       │   └── enums/               # UserRole, ItemStatus, ClaimStatus
│       └── resources/
│           ├── application.yml      # Main configuration
│           └── bootstrap.yml        # Config Server connection
│
├── lostlink-eureka/                 # Eureka Discovery Server
├── lostlink-gateway/                # Spring Cloud Gateway
├── lostlink-config/                 # Config Server
│
└── frontend/                        # React SPA
    ├── package.json
    ├── Dockerfile
    ├── nginx.conf
    ├── vite.config.js
    ├── tailwind.config.js
    └── src/
        ├── api/axios.js             # Axios with JWT interceptor
        ├── context/AuthContext.jsx   # Auth state management
        ├── components/              # Navbar, Footer, ProtectedRoute
        └── pages/                   # All page components
```

---

## Getting Started

### Prerequisites

- **Java 21** (Eclipse Temurin recommended)
- **Maven 3.9+**
- **MySQL 8.0** (or use Docker)
- **Node.js 20+** (for frontend)
- **Docker & Docker Compose** (optional, for containerized deployment)

### Option 1: Run with Docker (Recommended)

```bash
# Clone and navigate to the project
cd LostLink

# Build and start ALL services (MySQL + all microservices + frontend)
docker-compose up --build

# Access:
# Frontend:  http://localhost:3000
# API:       http://localhost:8081
# Gateway:   http://localhost:8080
# Eureka:    http://localhost:8761
# Swagger:   http://localhost:8081/swagger-ui.html
```

### Option 2: Run Locally (Development)

#### 1. Database Setup

```sql
CREATE DATABASE lostlink_db;
```

#### 2. Backend Services

```bash
# Terminal 1: Start Eureka
cd lostlink-eureka
mvn spring-boot:run

# Terminal 2: Start Core Service
cd lostlink-service
mvn spring-boot:run

# Terminal 3: Start Gateway (optional)
cd lostlink-gateway
mvn spring-boot:run
```

#### 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

#### 4. Access the Application

| Service | URL |
|---------|-----|
| React Frontend | http://localhost:3000 |
| LostLink API | http://localhost:8081 |
| Swagger UI | http://localhost:8081/swagger-ui.html |
| Eureka Dashboard | http://localhost:8761 |
| API Gateway | http://localhost:8080 |

---

## API Endpoints

### Authentication (Public)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and get JWT |
| GET | `/api/auth/profile` | Get current user profile |

### Lost Items
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | `/api/items` | Authenticated |
| GET | `/api/items` | Authenticated |
| GET | `/api/items/search` | Public |
| GET | `/api/items/{id}` | Public |
| GET | `/api/items/my-items` | Authenticated |
| PUT | `/api/items/{id}` | Owner only |
| DELETE | `/api/items/{id}` | Owner only |
| PUT | `/api/items/{id}/status` | SECURITY/ADMIN |

### Claims
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | `/api/claims` | Authenticated |
| GET | `/api/claims/my-claims` | Authenticated |
| GET | `/api/claims/item/{itemId}` | Authenticated |
| GET | `/api/claims/pending` | SECURITY/ADMIN |
| PUT | `/api/claims/review/{id}` | SECURITY/ADMIN |

### Dashboard
| Method | Endpoint | Access |
|--------|----------|--------|
| GET | `/api/dashboard/student` | Authenticated |
| GET | `/api/dashboard/security` | SECURITY/ADMIN |
| GET | `/api/dashboard/admin` | ADMIN |

### Admin
| Method | Endpoint | Access |
|--------|----------|--------|
| GET | `/api/admin/users` | ADMIN |
| PUT | `/api/admin/users/{id}/role` | ADMIN |
| DELETE | `/api/admin/users/{id}` | ADMIN |

---

## User Roles

| Role | Permissions |
|------|------------|
| **STUDENT** | Register, login, report/search items, file claims, view own data |
| **SECURITY** | View claims, approve/reject claims, update item status |
| **ADMIN** | Full access + user management, role assignment, all dashboards |

---

## Key Design Patterns

| Pattern | Implementation |
|---------|---------------|
| **Layered Architecture** | Controller → Service → Repository → Database |
| **DTO Pattern** | Entities never exposed to API; dedicated Request/Response DTOs |
| **Global Exception Handling** | `@RestControllerAdvice` for consistent error responses |
| **JWT Stateless Auth** | No HTTP sessions; token validated on every request |
| **Circuit Breaker** | Resilience4j prevents cascading failures in microservice calls |
| **Service Discovery** | Eureka enables dynamic service registration and lookup |
| **API Gateway** | Single entry point with routing, CORS, and future rate limiting |
| **Centralized Config** | Config Server for externalized configuration management |

---

## Database Schema

```
users           ← id, name, email, password (BCrypt), role, created_at, updated_at
lost_items      ← id, title, description, category, location, date_lost, status, image_url, reported_by (FK→users)
claims          ← id, claim_date, reason, status, student_id (FK→users), item_id (FK→lost_items)
notifications   ← id, message, type, read, user_id (FK→users), created_at
```

---

## Interview-Ready Topics

This project demonstrates proficiency in:

1. **Spring Boot 3** — Auto-configuration, starter dependencies, actuator
2. **REST API Design** — Proper HTTP methods, status codes, pagination, filtering
3. **Spring Security** — Filter chain, JWT authentication, role-based authorization
4. **Microservices** — Service discovery (Eureka), API Gateway, Config Server
5. **Resilience Patterns** — Circuit breaker with Resilience4j
6. **JPA/Hibernate** — Entity mapping, relationships, lazy loading, pagination
7. **Validation** — Bean Validation (Jakarta), custom error responses
8. **Swagger/OpenAPI** — Auto-generated API documentation
9. **Docker** — Multi-stage builds, Docker Compose orchestration
10. **React** — Functional components, hooks, context API, routing

---

## License

This project is for educational and demonstration purposes.
