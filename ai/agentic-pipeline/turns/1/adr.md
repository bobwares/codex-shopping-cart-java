# ADR 1: Adopt Spring Boot MVC with Liquibase and Postgres-Compatible Schema

**Status**: Accepted

**Date**: 2025-10-02

## Context
The Shopping Cart API must provide RESTful operations with persistent storage and needs a maintainable schema evolution strategy. We must pick a framework stack, configuration approach, and database migration tooling that align with the provided domain schema and enterprise conventions.

## Decision
Adopt Spring Boot MVC with Spring Data JPA for the service layer, Liquibase for database migrations, and PostgreSQL-compatible SQL scripts. Provide H2 and Testcontainers fallbacks for tests, exposing AppProperties for configurable defaults.

## Consequences
- Positive: Familiar Spring ecosystem with auto-configuration accelerates development; Liquibase ensures consistent schema evolution; AppProperties centralizes defaults.
- Negative: Requires managing Liquibase changelogs and ensuring Testcontainers Docker availability for integration tests.
- Follow-up: Expand automated tests for edge cases and enhance Testcontainers configuration for CI Docker support.
