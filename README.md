# 🥗 Food Rescue Optimizer

> AI-powered food rescue logistics platform — built with Java 21, Spring Boot, Timefold constraint solver, and deployed on AWS ECS.
The Problem
Over 1.3 billion tons of food is wasted globally every year while millions go hungry. The gap isn't food — it's logistics. Donors don't know where to send food. Recipients don't know what's available. And no one is optimizing the routes.

The Solution
Food Rescue Optimizer connects food donors (restaurants, supermarkets) with recipients (food banks, shelters) and uses AI to plan the most efficient pickup and delivery routes — minimizing distance, saving fuel, and ensuring food reaches people before it spoils.

How It Works
Donor registers and submits available food items with quantity and expiry time
ML model scores spoilage risk instantly based on food category, temperature, and hours until expiry
AI optimizer assigns vehicles to pickups using constraint-based optimization
Routes are served via REST API for downstream apps to consume
Key Features
🤖 AI Route Optimizer — Timefold constraint solver assigns vehicles to pickups across 3 hard constraints: vehicle capacity, time windows, and distance minimization
🧠 ML Spoilage Scorer — logistic regression model (Smile library) predicts spoilage risk at submission time using food category, temperature, and hours until expiry
☁️ Cloud Native — containerized with Docker, deployed on AWS ECS Fargate with RDS PostgreSQL
🔄 CI/CD Pipeline — GitHub Actions automatically builds, tests, and redeploys on every push to main
📋 API Documentation — Swagger/OpenAPI interactive docs with all 11 endpoints
🗄️ Zero Schema Drift — versioned Flyway migrations ensure identical database state from local to production
✅ Tested — unit tested with JUnit 5 and Mockito
Benchmark — Solver Results
Metric	Baseline (nearest-neighbor)	Timefold Solver
Total route distance	TODO km	TODO km
Vehicles used	TODO	TODO
Solve time	—	TODO s
Sample scenario: TODO donors, TODO vehicles, TODO recipients.

Tech Stack
Layer	Technology
Language	Java 21
Framework	Spring Boot 3.4.4
AI Optimizer	Timefold Solver
ML Library	Smile (Logistic Regression)
Database	PostgreSQL + Flyway
Containerization	Docker
Cloud	AWS ECS Fargate + RDS
CI/CD	GitHub Actions
API Docs	Swagger / OpenAPI
API Endpoints
Method	Endpoint	Description
POST	/api/donors	Register a food donor
GET	/api/donors	List all donors
GET	/api/donors/{id}	Get donor by ID
POST	/api/donors/{id}/food-items	Submit a food item with ML scoring
POST	/api/recipients	Register a recipient
GET	/api/recipients	List all recipients
GET	/api/recipients/{id}	Get recipient by ID
POST	/api/vehicles	Add a vehicle
GET	/api/vehicles	List all vehicles
GET	/api/vehicles/{id}	Get vehicle by ID
POST	/api/optimize	Run the AI route optimizer
Live Demo
Resource	URL
🌐 Live API	http://3.238.51.54:8081
📖 API Docs	http://3.238.51.54:8081/swagger-ui/index.html
💻 GitHub	https://github.com/erdkash1/Food-Rescue-Optimizer
Getting Started
Prerequisites
Java 21
Docker + Colima (Mac) or Docker Desktop
PostgreSQL
Run Locally
bash


colima start
docker compose up -d
./mvnw spring-boot:run
Run Tests
bash


./mvnw test
Demo Data
Once the seed migration is in place, a fresh clone loads the demo dataset automatically via Flyway — then call POST /api/optimize to watch the solver work.

Project Structure


src/main/java/com/foodrescue/optimizer/
├── controller/      # REST endpoints — Donor, FoodItem, Recipient, Vehicle, RouteOptimizer
├── service/         # Business logic — RouteOptimizerService, SpoilageRiskScorer (ML)
├── solver/          # Timefold constraints — FoodRescueConstraintProvider
├── domain/          # Planning entities — RoutePlan, Route, RouteStop, Vehicle, Donor, ...
├── repository/      # Spring Data JPA repositories
└── exception/       # GlobalExceptionHandler

src/main/resources/
├── application.properties.example
└── db/migration/    # Flyway — V1 init schema, V2 remaining tables, V3 spoilage fields


src/main/resources/
├── application.properties.example
└── db/migration/    # Flyway — V1 init schema, V2 remaining tables, V3 spoilage fields
CI/CD
Every push to main automatically:

Sets up Java 21
Builds with mvn clean package
Builds the Docker image and pushes it to Amazon ECR
Triggers a new deployment of the ECS service

Architecture
flowchart LR
Client -->|REST| C[Controllers<br/>Donor, FoodItem, Recipient,<br/>Vehicle, RouteOptimizer]
C --> S[Services<br/>DonorService, FoodItemService,<br/>RecipientService, VehicleService]
S --> ML[SpoilageRiskScorer<br/>Smile logistic regression]
C --> Opt[RouteOptimizerService]
Opt --> Solver[Timefold Solver<br/>FoodRescueConstraintProvider]
Solver --> Plan[RoutePlan]
S --> DB[(PostgreSQL<br/>Flyway migrations)]
Opt --> DB
