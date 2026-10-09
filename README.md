# 🛒 E-Commerce Back-End API

A production-ready RESTful e-commerce API built with Java and Spring Boot, featuring JWT authentication, role-based access control, and full order management. Designed and developed independently as a portfolio project to demonstrate real-world backend engineering skills.

<!-- TODO: after redeploying, replace YOUR-APP below with the real Railway URL everywhere in this file -->

[![Live Demo](https://img.shields.io/badge/Live--Demo-brightgreen)](https://YOUR-APP.up.railway.app)
[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Containerized-blue)](https://www.docker.com/)

## Live Demo

**Base URL:** `https://YOUR-APP.up.railway.app`

The API is live and fully operational. Use the endpoints below with a tool like Postman to explore the functionality.

## Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.5 |
| **Security** | Spring Security + JWT (JJWT 0.11.5) |
| **Database** | PostgreSQL |
| **ORM** | Spring Data JPA + Hibernate |
| **Build Tool** | Maven |
| **Containerization** | Docker + Docker Compose |
| **Deployment** | Railway |

## Features

* **JWT Authentication** — Stateless authentication using signed JWT tokens with BCrypt password hashing.
* **Role-Based Access Control** — Two-tier access control with `CUSTOMER` and `ADMIN` roles protecting all sensitive endpoints.
* **Product Catalog** — Full CRUD operations with category filtering and real-time inventory tracking.
* **Shopping Cart** — Persistent cart management with add, remove, and clear operations.
* **Order Management** — Order placement from cart with automatic total calculation and price snapshot at time of purchase.
* **Order Status Tracking** — Five-stage order lifecycle: `PENDING` → `CONFIRMED` → `SHIPPED` → `DELIVERED` → `CANCELLED`.
* **Global Exception Handling** — Consistent error responses with meaningful HTTP status codes.
* **Dockerized** — Fully containerized with multi-stage Dockerfile and Docker Compose for one-command local setup.

## Architecture

```mermaid
flowchart LR
    Client -->|HTTPS| API[Spring Boot API]
    
    subgraph API[Spring Boot API]
        direction TB
        Sec[Spring Security<br/>JWT filter + RBAC] --> Ctrl[Controllers<br/>Auth, Product, Cart, Order]
        Ctrl --> Svc[Service layer<br/>Business logic]
        Svc --> Repo[Spring Data JPA<br/>Repositories]
    end
    
    Repo -->|JPA / Hibernate| DB[(PostgreSQL)]
```

### Project Structure
```text
src/main/java/com/iggy/ecommerce/
├── controller/       # REST API endpoints — handles HTTP requests and responses
├── service/          # Business logic layer — core application logic
├── repository/       # Data access layer — Spring Data JPA repositories
├── entity/           # JPA entities — database table mappings
├── dto/              # Data Transfer Objects — request and response payloads
├── security/         # JWT filter, UserDetailsService, and SecurityConfig
└── exception/        # Custom exceptions and GlobalExceptionHandler
```

### Database Schema — 6 Tables:
```text
users ──────── carts ──────── cart_items ──── products
  │                                               │
  └──────── orders ──────── order_items ──────────┘
```

## Getting Started

### Prerequisites
* Java 21
* Maven
* Docker *(for Option 1)*
* PostgreSQL *(for Option 2)*

### Option 1 — Run with Docker (Recommended)
The easiest way to run the project locally. No need to install PostgreSQL separately.

```bash
# Clone the repository
git clone [https://github.com/erdkash1/Ecommerce.git](https://github.com/erdkash1/Ecommerce.git)
cd Ecommerce

# Build the JAR file
./mvnw clean package -DskipTests

# Build the Docker image
docker build -t ecommerce-app .

# Start the full stack (Spring Boot + PostgreSQL)
docker compose up
```
The API will be available at `http://localhost:8080`

### Option 2 — Run Locally without Docker

```bash
# Clone the repository
git clone [https://github.com/erdkash1/Ecommerce.git](https://github.com/erdkash1/Ecommerce.git)
cd Ecommerce
```

Create a PostgreSQL database:
```sql
CREATE DATABASE ecommerce_db;
```

Configure `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret=your_base64_encoded_secret
jwt.expiration=86400000
```

Run the application:
```bash
./mvnw spring-boot:run
```
The API will be available at `http://localhost:8080`

## Authentication

This API uses stateless JWT authentication. To access protected endpoints:

1. **Step 1** — Register or login to receive a JWT token.
2. **Step 2** — Include the token in the `Authorization` header of every subsequent request:
   ```text
   Authorization: Bearer your_token_here
   ```

*Tokens expire after 24 hours and must be refreshed by logging in again.*

## API Reference

### Authentication Endpoints
| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :---: |
| `POST` | `/auth/register` | Register a new user account | ❌ |
| `POST` | `/auth/login` | Login and receive JWT token | ❌ |

### Product Endpoints
| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :---: |
| `GET` | `/products` | Retrieve all products | ✅ |
| `GET` | `/products/{id}` | Retrieve product by ID | ✅ |
| `GET` | `/products/category/{category}` | Filter products by category | ✅ |
| `POST` | `/products` | Create a new product | 🔒 Admin |
| `PUT` | `/products/{id}` | Update an existing product | 🔒 Admin |
| `DELETE` | `/products/{id}` | Delete a product | 🔒 Admin |

### Cart Endpoints
| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :---: |
| `GET` | `/carts/{userId}` | Retrieve user's cart | ✅ |
| `POST` | `/carts/{userId}/items` | Add item to cart | ✅ |
| `DELETE` | `/carts/{userId}/items/{cartItemId}`| Remove item from cart | ✅ |
| `DELETE` | `/carts/{cartId}/clear` | Clear entire cart | ✅ |

### Order Endpoints
| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :---: |
| `POST` | `/orders/{userId}` | Place order from cart | ✅ |
| `GET` | `/orders/user/{userId}`| Get all orders for a user | ✅ |
| `GET` | `/orders/{orderId}` | Get order by ID | ✅ |
| `PUT` | `/orders/{orderId}/status`| Update order status | 🔒 Admin |

## Example Requests

### Register a New User
```http
POST [https://YOUR-APP.up.railway.app/auth/register](https://YOUR-APP.up.railway.app/auth/register)
Content-Type: application/json

{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
}
```
**Response:**
```json
{
    "token": "eyJhbGciOiJIUzM4NCJ9..."
}
```

### Add Item to Cart
```http
POST [https://YOUR-APP.up.railway.app/carts/1/items?productId=1&quantity=2](https://YOUR-APP.up.railway.app/carts/1/items?productId=1&quantity=2)
Authorization: Bearer your_token_here
```

### Place an Order
```http
POST [https://YOUR-APP.up.railway.app/orders/1](https://YOUR-APP.up.railway.app/orders/1)
Authorization: Bearer your_token_here
```

## Error Handling

The API returns consistent error responses across all endpoints:

| HTTP Status | Meaning | Description |
| :--- | :--- | :--- |
| **200 OK** | Request successful | The request was processed successfully. |
| **201 Created** | Resource created | The resource was created successfully. |
| **400 Bad Request** | Invalid input | Invalid input parameters or business rule violation. |
| **401 Unauthorized** | Missing token | Missing or invalid JWT token. |
| **403 Forbidden** | Access denied | Insufficient role permissions. |
| **404 Not Found** | Resource missing | Requested resource does not exist. |
| **500 Server Error** | Unexpected error | Unexpected server error occurred. |

## Testing
REST Assured API tests covering full CRUD operations with JWT authentication live in the `qa-automation-practice` repository.

## CI/CD
Every push to `main` automatically:
1. Sets up Java 21
2. Starts a PostgreSQL 15 service container
3. Runs `mvn verify` — full build plus all tests

*View workflow runs in the Actions tab.*

## Author
**Erdenesuren Shirmen**  
CS Graduate — Missouri State University (Graduated July 2026)  
GitHub: [erdkash1](https://github.com/erdkash1)