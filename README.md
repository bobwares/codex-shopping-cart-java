<!--
/**
 * App: Shopping Cart API
 * Package: documentation
 * File: README.md
 * Version: 0.2.0
 * Turns: 1, 2
 * Author: gpt-5-codex
 * Date: 2025-10-03T00:25:40Z
 * Exports: Project overview documentation
 * Description: Describes the Shopping Cart API project, local development workflow, and supporting tooling.
 */
-->
# Shopping Cart API

The Shopping Cart API is a Spring Boot service that exposes RESTful endpoints for managing shopping carts, including items, pricing, and discount information.

## Local Development

### Prerequisites
- Java 21
- Maven 3.9+
- Docker Desktop or Docker Engine 24+

### Environment Variables
Create a `.env` file in the project root with the following values before starting local services:

```
APP_NAME=shopping-cart-api
APP_PORT=8080
DATABASE_HOST=localhost
DATABASE_PORT=5432
DATABASE_NAME=shopping_cart
DATABASE_USERNAME=shopping_cart
DATABASE_PASSWORD=shopping_cart
DATABASE_SCHEMA=public
```

### Database (Docker Compose)
Use the provided Compose definition to launch PostgreSQL locally:

```
docker compose up -d postgres
```

The container exposes PostgreSQL on `localhost:${DATABASE_PORT}` and mounts `db/init` for optional initialization scripts. Use `docker compose down -v` to remove volumes when resetting the environment.

### Application

Run the application with Maven once the database is available:

```
mvn spring-boot:run
```

### Testing

```
mvn test
```

## API Documentation
Springdoc OpenAPI exposes API documentation at `http://localhost:${APP_PORT}/swagger-ui.html` when the application is running.
