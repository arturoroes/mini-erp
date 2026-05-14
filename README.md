# Mini ERP — Inventory and Sales Management API

A REST API simulating the core domain of an enterprise resource planning
(ERP) system: product catalogue, suppliers, customers, and sales orders
with inventory management. Built as a personal learning project to deepen
my understanding of Spring Boot, layered architecture, and the patterns
used in real enterprise systems such as SAP.

> **Status:** work in progress. This is an actively developed learning
> project. See the [Roadmap](#roadmap) section for what is implemented
> and what is still pending.

---

## Stack

- **Java 21** — language and runtime
- **Spring Boot 4** — application framework
- **Spring Data JPA + Hibernate** — persistence layer
- **PostgreSQL 16** — relational database
- **Maven** — build and dependency management
- **Lombok** — boilerplate reduction
- **Bean Validation (Jakarta Validation)** — request validation
- **Docker Compose** — local infrastructure (PostgreSQL container)

---

## Architecture

The project follows a classic layered architecture with strict
separation of concerns:

```
HTTP Request
    ↓
Controller   ← REST endpoints, request validation, HTTP responses
    ↓
Service      ← business logic, transactions, domain rules
    ↓
Repository   ← data access via Spring Data JPA
    ↓
Database     ← PostgreSQL
```

Additional design decisions:

- **DTOs** decouple the API contract from JPA entities, avoiding
  serialisation cycles, exposing only what each endpoint needs, and
  allowing distinct request/response shapes per operation.
- **Mappers** centralise conversion between entities and DTOs.
- **Custom exceptions** (`ResourceNotFoundException`,
  `DuplicateResourceException`) flow up from the service layer and are
  translated into proper HTTP responses (404, 409, etc.) by a global
  `@RestControllerAdvice` handler.
- **Validation** is declarative via Jakarta annotations
  (`@NotBlank`, `@Size`, `@Email`) on request DTOs, with field-level
  error responses produced by the global exception handler.

---

## Domain model

```
Category   1───N   Product
Supplier   1───N   Product
Customer   1───N   Order
Order      1───N   OrderLine
Product    1───N   OrderLine
```

Notable design decisions in the data model:

- `OrderLine.unitPrice` is stored on the line itself (not referenced
  from `Product.price`) so historical orders preserve the price at the
  time of sale, even if the product price changes later. This is the
  standard pattern in real ERPs.
- `Order.status` is an enum (`DRAFT`, `CONFIRMED`, `SHIPPED`,
  `CANCELLED`) persisted as a `STRING` to avoid the ordinal pitfall.
- All monetary fields use `BigDecimal` (not `double`) to avoid floating
  point precision errors.
- All FK relationships use `FetchType.LAZY` to prevent N+1 query issues
  on common reads.

---

## Project structure

```
src/main/java/com/arturoroes/minierp/
├── controller/   REST endpoints
├── service/      business logic
├── repository/   Spring Data JPA interfaces
├── entity/       JPA entities (mapped to DB tables)
├── dto/          request and response data transfer objects
├── mapper/       entity ↔ DTO conversions
└── exception/    custom exceptions + global handler
```

---

## Running locally

### Prerequisites
- JDK 21
- Maven 3.9+
- Docker and Docker Compose

### Steps

1. **Start the database**

```bash
   docker compose up -d
```

   This starts PostgreSQL 16 on `localhost:5432` with database `minierp`
   and user `minierp`.

2. **Run the application**

```bash
   ./mvnw spring-boot:run
```

   The API will be available at `http://localhost:8080`.

3. **Verify**

```bash
   curl http://localhost:8080/api/categories
```

   Should return `[]` on a fresh database.

---

## API endpoints (implemented)

### Categories — `/api/categories`

| Method | Endpoint               | Description           |
|--------|------------------------|-----------------------|
| GET    | `/api/categories`      | List all categories   |
| GET    | `/api/categories/{id}` | Get a single category |
| POST   | `/api/categories`      | Create category       |
| PUT    | `/api/categories/{id}` | Update category       |
| DELETE | `/api/categories/{id}` | Delete category       |

### Suppliers — `/api/suppliers`

Same CRUD shape as `/api/categories`, with optional `taxId`, `email`,
`phone`, and `country` fields.

### Error responses

The API returns structured error responses with consistent shape:

```json
{
  "timestamp": "2026-05-14T20:35:12",
  "status": 404,
  "error": "Not Found",
  "message": "Category not found with id: '99'"
}
```

For validation errors, individual field errors are included:

```json
{
  "timestamp": "2026-05-14T20:35:12",
  "status": 400,
  "error": "Validation failed",
  "fieldErrors": {
    "name": "Name is required",
    "email": "must be a well-formed email address"
  }
}
```

---

## Roadmap

### Implemented
- [x] JPA entities for the full domain model (Category, Supplier,
      Customer, Product, Order, OrderLine)
- [x] Spring Data JPA repositories for all entities
- [x] Layered architecture (Controller → Service → Repository) with
      DTOs, mappers, and global exception handling
- [x] Full CRUD for `Category` with validation and uniqueness checks
- [x] Full CRUD for `Supplier` with validation and uniqueness checks

### In progress / pending
- [ ] Full CRUD for `Customer`
- [ ] Full CRUD for `Product` (including FK validation against
      Category and Supplier)
- [ ] `Order` and `OrderLine` with business logic: stock validation,
      inventory decrement on confirm, total calculation, status
      transitions
- [ ] Query endpoints with filtering and pagination
- [ ] Unit tests (JUnit 5 + Mockito) on the service layer
- [ ] OpenAPI / Swagger documentation via springdoc
- [ ] Dockerfile and full `docker-compose.yml` for one-command
      deployment
- [ ] Database migrations with Flyway (replacing `ddl-auto=update`)

---

## What I learned

This project has been a deliberate exercise in moving beyond simple
CRUD tutorials and writing code closer to what a real Spring backend
team would expect. Concretely:

- Why each layer exists and what belongs in each one, instead of
  cramming logic into controllers.
- The difference between exposing JPA entities directly and using DTOs
  to define a clean API contract.
- How `@Transactional` and `@RestControllerAdvice` change the way error
  handling and consistency flow through an application.
- Designing entities with realistic ERP concerns in mind (price
  snapshotting on order lines, enum persistence strategy,
  `BigDecimal` for money, lazy fetching to avoid N+1).
- Why this matters for SAP: the architecture and design patterns
  practised here map directly to how SAP customisations are structured
  on top of S/4HANA — separation of model, business logic, and UI;
  controlled state transitions; and disciplined transactional
  boundaries.

---

## About

Built by **Arturo Rodríguez Escudero** as part of a focused effort to
move into junior consulting roles in the SAP ecosystem.

- LinkedIn: [arturoroes](https://www.linkedin.com/in/arturoroes)
- Email: arturo.roes@outlook.com
