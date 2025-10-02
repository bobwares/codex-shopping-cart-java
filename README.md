<!--
App: Shopping Cart API
Package: root
File: README.md
Version: 0.1.0
Turns: [1]
Author: Codex Agent <agent@local>
Date: 2025-10-02T10:47:14Z
Exports: Project overview documentation
Description: Provides an overview of the Shopping Cart API service, build
             instructions, and CI/CD status badges.
-->

# Shopping Cart API

[![CI](https://github.com/<owner>/<repo>/actions/workflows/ci.yml/badge.svg)](https://github.com/<owner>/<repo>/actions/workflows/ci.yml)
[![Release](https://github.com/<owner>/<repo>/actions/workflows/release.yml/badge.svg)](https://github.com/<owner>/<repo>/actions/workflows/release.yml)

The Shopping Cart API is a Spring Boot service that manages shopping cart
operations for e-commerce platforms. It exposes RESTful endpoints for
creating, retrieving, updating, and deleting shopping carts while persisting
state in PostgreSQL.

## Getting Started

```bash
./mvnw spring-boot:run
```

### Running Tests

```bash
./mvnw clean verify
```

### Database Assets

- SQL migrations are stored in `db/migrations`.
- Seed data scripts are in `db/scripts`.
