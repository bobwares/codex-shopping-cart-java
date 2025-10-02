# Architecture Decision Record

Spring Boot Scaffold Configuration Choices

**Turn**: 1

**Status**: Accepted

**Date**: 2025-10-02 - 23:02

**Context**
The Shopping Cart API project required an initial Spring Boot scaffold aligned with the spring-boot-mvc-jpa-postgresql pattern. The application must expose validated configuration properties, provide observability endpoints, and compile under Java 21.

**Options Considered***
1. Generate a minimal scaffold limited to application name and port properties.
2. Extend the configuration properties to include tax, shipping, and currency metadata referenced in application.yml to avoid runtime binding gaps.

**Decision**
Implemented option 2 by modelling tax rate, shipping cost, and supported currencies in `AppProperties` with validation annotations. This aligns the strongly typed configuration with the required placeholders in application.yml and maintains pattern compliance.

**Result**
- `AppProperties` exposes additional validated fields mapped to the configuration file.
- `MetaController` and unit tests rely on the typed configuration to supply metadata endpoints.

**Consequences**
- Ensures application startup validation succeeds when configuration values are provided.
- Introduces BigDecimal and collection handling that developers must maintain in future changes.
- Provides a foundation for extending business logic around pricing and localization in later turns.
