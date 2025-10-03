# Project Changelog

## Turn 1 – 2025-10-02
- Initialized Spring Boot project scaffold with Maven configuration, application properties, and baseline controller/test.
- Added supporting configuration files (.gitignore, README-config.md, Liquibase changelog, actuator HTTP requests).
- Recorded agentic pipeline metadata for ongoing turn tracking.

## Turn 2 – 2025-10-03
- Added Docker Compose support, database documentation, and Liquibase integration for the shopping cart schema.
- Modeled shopping cart domain entities with Spring Data JPA, repository, service, REST controller, and error handling.
- Implemented SQL migrations, seed data scripts, and end-to-end HTTP workflow covering CRUD scenarios.
- Created unit and integration tests using Mockito and Testcontainers to validate persistence and API behavior.
