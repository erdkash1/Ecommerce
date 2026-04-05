# E-Commerce Back-End API

A RESTful e-commerce back-end API built with Spring Boot. This project was built as a portfolio project to demonstrate real-world back-end development skills including authentication, authorization, and business logic.

## Live Demo

Base URL: `https://ecommerce-production-0b9e.up.railway.app`

## Tech Stack

- **Java 21**
- **Spring Boot 3.5**
- **Spring Security + JWT** for authentication
- **Spring Data JPA + Hibernate** for database access
- **PostgreSQL** for the database
- **Maven** for dependency management
- **Docker** for containerization
- **Deployed on Railway**

## Features

- User registration and login with JWT authentication
- Role-based access control (CUSTOMER and ADMIN roles)
- Product catalog with category filtering and inventory tracking
- Shopping cart — add, remove, and clear items
- Order placement from cart with automatic total calculation
- Order status tracking (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
- Proper error handling with meaningful HTTP status codes
- Fully Dockerized with Docker Compose

## Project Structure

```
src/main/java/com/iggy/ecommerce/
├── controller/       # REST API endpoints
├── service/          # Business logic
├── repository/       # Database access
├── entity/           # JPA entities
├── dto/              # Data Transfer Objects
├── security/         # JWT and Spring Security config
└── exception/        # Custom exceptions and global handler
```

## Getting Started

### Prerequisites
- Java 21
- PostgreSQL
- Maven
- Docker (optional)

### Option 1 — Run with Docker (Recommended)

Make sure Docker is installed, then run:

```bash
docker compose up
```

The API will start on `http://localhost:8080` — no need to install PostgreSQL separately!

### Option 2 — Run Locally without Docker

1. Clone the repository
```bash
git clone https://github.com/erdkash1/Ecommerce.git
cd Ecommerce
```

2. Create a PostgreSQL database
```sql
CREATE DATABASE ecommerce_db;
```

3. Update `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret=your_base64_encoded_secret
jwt.expiration=86400000
```

4. Run the application
```bash
./mvnw spring-boot:run
```

The API will start on `http://localhost:8080`

## Authentication

This API uses JWT authentication. To access protected endpoints:

1. Register or login to get a token
2. Add the token to the `Authorization` header of every request:
```
Authorization: Bearer your_token_here
```

## API Endpoints

### Auth
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/auth/register` | Register a new user | No |
| POST | `/auth/login` | Login and get JWT token | No |

### Products
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/products` | Get all products | Yes |
| GET | `/products/{id}` | Get product by ID | Yes |
| GET | `/products/category/{category}` | Get products by category | Yes |
| POST | `/products` | Create a product | Admin only |
| PUT | `/products/{id}` | Update a product | Admin only |
| DELETE | `/products/{id}` | Delete a product | Admin only |

### Cart
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/carts/{userId}` | Get user's cart | Yes |
| POST | `/carts/{userId}/items` | Add item to cart | Yes |
| DELETE | `/carts/{userId}/items/{cartItemId}` | Remove item from cart | Yes |
| DELETE | `/carts/{cartId}/clear` | Clear the cart | Yes |

### Orders
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/orders/{userId}` | Place an order from cart | Yes |
| GET | `/orders/user/{userId}` | Get all orders for a user | Yes |
| GET | `/orders/{orderId}` | Get order by ID | Yes |
| PUT | `/orders/{orderId}/status` | Update order status | Admin only |

## Example Requests

### Register
**POST** `https://ecommerce-production-0b9e.up.railway.app/auth/register`
```json
{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
}
```

### Add item to cart
```
POST https://ecommerce-production-0b9e.up.railway.app/carts/1/items?productId=1&quantity=2
Authorization: Bearer your_token_here
```

### Place an order
```
POST https://ecommerce-production-0b9e.up.railway.app/orders/1
Authorization: Bearer your_token_here
```

## Author

Erdenesuren Shirmen — Senior Computer Science Student
GitHub: [@erdkash1](https://github.com/erdkash1)