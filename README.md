# E-Commerce Back-End API

A production-ready RESTful e-commerce API built with Java and Spring Boot, featuring JWT authentication, role-based access control, and full order management. Designed and developed independently as a portfolio project to demonstrate real-world backend engineering skills.

[![Live Demo](https://img.shields.io/badge/Live-Demo-brightgreen)](https://ecommerce-production-0b9e.up.railway.app)
[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Containerized-blue)](https://www.docker.com/)
 
---

## Live Demo

**Base URL:** `https://ecommerce-production-0b9e.up.railway.app`

> The API is live and fully operational. Use the endpoints below with a tool like Postman to explore the functionality.
 
---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security + JWT (JJWT 0.11.5) |
| Database | PostgreSQL |
| ORM | Spring Data JPA + Hibernate |
| Build Tool | Maven |
| Containerization | Docker + Docker Compose |
| Deployment | Railway |
 
---

## Features

- **JWT Authentication** — Stateless authentication using signed JWT tokens with BCrypt password hashing
- **Role-Based Access Control** — Two-tier access control with CUSTOMER and ADMIN roles protecting all sensitive endpoints
- **Product Catalog** — Full CRUD operations with category filtering and real-time inventory tracking
- **Shopping Cart** — Persistent cart management with add, remove, and clear operations
- **Order Management** — Order placement from cart with automatic total calculation and price snapshot at time of purchase
- **Order Status Tracking** — Five-stage order lifecycle: PENDING → CONFIRMED → SHIPPED → DELIVERED → CANCELLED
- **Global Exception Handling** — Consistent error responses with meaningful HTTP status codes
- **Dockerized** — Fully containerized with multi-stage Dockerfile and Docker Compose for one-command local setup
---

## Architecture

```
src/main/java/com/iggy/ecommerce/
├── controller/       # REST API endpoints — handles HTTP requests and responses
├── service/          # Business logic layer — core application logic
├── repository/       # Data access layer — Spring Data JPA repositories
├── entity/           # JPA entities — database table mappings
├── dto/              # Data Transfer Objects — request and response payloads
├── security/         # JWT filter, UserDetailsService, and SecurityConfig
└── exception/        # Custom exceptions and GlobalExceptionHandler
```

**Database Schema — 6 Tables:**
```
users ──────── carts ──────── cart_items ──── products
  │                                               │
  └──────── orders ──────── order_items ──────────┘
```
 
---

## Getting Started

### Prerequisites
- Java 21
- Maven
- Docker (for Option 1)
- PostgreSQL (for Option 2)
---

### Option 1 — Run with Docker (Recommended)

The easiest way to run the project locally. No need to install PostgreSQL separately.

```bash
# Clone the repository
git clone https://github.com/erdkash1/Ecommerce.git
cd Ecommerce
 
# Build the JAR file
./mvnw clean package -DskipTests
 
# Build the Docker image
docker build -t ecommerce-app .
 
# Start the full stack (Spring Boot + PostgreSQL)
docker compose up
```

The API will be available at `http://localhost:8080`
 
---

### Option 2 — Run Locally without Docker

```bash
# Clone the repository
git clone https://github.com/erdkash1/Ecommerce.git
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
 
---

## Authentication

This API uses **stateless JWT authentication**. To access protected endpoints:

**Step 1** — Register or login to receive a JWT token

**Step 2** — Include the token in the `Authorization` header of every subsequent request:
```
Authorization: Bearer your_token_here
```

Tokens expire after **24 hours** and must be refreshed by logging in again.
 
---

## API Reference

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|:---:|
| POST | `/auth/register` | Register a new user account | ❌ |
| POST | `/auth/login` | Login and receive JWT token | ❌ |

### Product Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|:---:|
| GET | `/products` | Retrieve all products | ✅ |
| GET | `/products/{id}` | Retrieve product by ID | ✅ |
| GET | `/products/category/{category}` | Filter products by category | ✅ |
| POST | `/products` | Create a new product | 🔒 Admin |
| PUT | `/products/{id}` | Update an existing product | 🔒 Admin |
| DELETE | `/products/{id}` | Delete a product | 🔒 Admin |

### Cart Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|:---:|
| GET | `/carts/{userId}` | Retrieve user's cart | ✅ |
| POST | `/carts/{userId}/items` | Add item to cart | ✅ |
| DELETE | `/carts/{userId}/items/{cartItemId}` | Remove item from cart | ✅ |
| DELETE | `/carts/{cartId}/clear` | Clear entire cart | ✅ |

### Order Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|:---:|
| POST | `/orders/{userId}` | Place order from cart | ✅ |
| GET | `/orders/user/{userId}` | Get all orders for a user | ✅ |
| GET | `/orders/{orderId}` | Get order by ID | ✅ |
| PUT | `/orders/{orderId}/status` | Update order status | 🔒 Admin |
 
---

## Example Requests

### Register a New User
```http
POST https://ecommerce-production-0b9e.up.railway.app/auth/register
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
POST https://ecommerce-production-0b9e.up.railway.app/carts/1/items?productId=1&quantity=2
Authorization: Bearer your_token_here
```

### Place an Order
```http
POST https://ecommerce-production-0b9e.up.railway.app/orders/1
Authorization: Bearer your_token_here
```
 
---

## Error Handling

The API returns consistent error responses across all endpoints:

| HTTP Status | Meaning |
|---|---|
| 200 OK | Request successful |
| 201 Created | Resource created successfully |
| 400 Bad Request | Invalid input or business rule violation |
| 401 Unauthorized | Missing or invalid JWT token |
| 403 Forbidden | Insufficient role permissions |
| 404 Not Found | Requested resource does not exist |
| 500 Internal Server Error | Unexpected server error |
 
---

## Author

**Erdenesuren Shirmen**
Senior Computer Science Student — Missouri State University (Graduating July 2026)

GitHub:(https://github.com/erdkash1)
LinkedIn: https://linkedin.com/in/erdenesuren-shirmen-0912b425b)