# ADR 1: Establish Spring Boot Shopping Cart API Baseline

**Status**: Accepted

**Date**: 2025-10-01

## Context
The project requires a production-ready Shopping Cart API implemented with the spring-boot-mvc-jpa-postgresql pattern. No prior source code existed, necessitating a full project scaffold, database schema design, persistence mapping, and HTTP surface area.

## Decision
Bootstrap a Spring Boot 3.5.5 application using Maven with PostgreSQL, Liquibase, and Testcontainers. Model the shopping cart domain via normalized tables and JPA entities, expose CRUD operations through a Spring MVC controller, and document/test the workflow with OpenAPI annotations, unit tests, integration tests, and an `.http` script. App configuration is sourced from environment variables managed by `AppProperties` and `.env`.

## Consequences
- Provides a coherent, testable baseline that aligns with the prescribed implementation pattern and governance metadata requirements.
- Liquibase and SQL assets must be maintained alongside JPA mappings to avoid drift.
- Testcontainers introduces heavier integration tests but guarantees repeatable Postgres coverage.
