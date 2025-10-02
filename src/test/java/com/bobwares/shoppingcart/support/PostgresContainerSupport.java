/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.support
 * File: PostgresContainerSupport.java
 * Version: 0.1.0
 * Turns: [1]
 * Author: Codex Agent <agent@local>
 * Date: 2025-10-02T10:47:14Z
 * Exports: PostgresContainerSupport
 * Description: Provides a shared PostgreSQL Testcontainers configuration for
 *              integration and service tests.
 */
package com.bobwares.shoppingcart.support;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
public abstract class PostgresContainerSupport {

    private static final boolean DOCKER_AVAILABLE = isDockerAvailable();

    @Container
    protected static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
        .withDatabaseName("shopping_cart")
        .withUsername("shopping_cart")
        .withPassword("shopping_cart");

    private static boolean isDockerAvailable() {
        try {
            POSTGRES.getDockerImageName();
            return true;
        } catch (Throwable throwable) {
            return false;
        }
    }

    @DynamicPropertySource
    static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        if (DOCKER_AVAILABLE) {
            registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
            registry.add("spring.datasource.username", POSTGRES::getUsername);
            registry.add("spring.datasource.password", POSTGRES::getPassword);
            registry.add("spring.liquibase.enabled", () -> true);
            registry.add("spring.jpa.show-sql", () -> false);
            registry.add("DATABASE_HOST", POSTGRES::getHost);
            registry.add("DATABASE_PORT", () -> POSTGRES.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT).toString());
            registry.add("DATABASE_NAME", POSTGRES::getDatabaseName);
            registry.add("DATABASE_USERNAME", POSTGRES::getUsername);
            registry.add("DATABASE_PASSWORD", POSTGRES::getPassword);
        } else {
            registry.add("spring.datasource.url", () -> "jdbc:h2:mem:shopping_cart;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");
            registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
            registry.add("spring.datasource.username", () -> "sa");
            registry.add("spring.datasource.password", () -> "");
            registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
            registry.add("spring.jpa.show-sql", () -> false);
            registry.add("spring.liquibase.enabled", () -> false);
            registry.add("DATABASE_HOST", () -> "localhost");
            registry.add("DATABASE_PORT", () -> "5432");
            registry.add("DATABASE_NAME", () -> "shopping_cart");
            registry.add("DATABASE_USERNAME", () -> "shopping_cart");
            registry.add("DATABASE_PASSWORD", () -> "shopping_cart");
        }
        registry.add("APP_NAME", () -> "shopping-cart-api");
        registry.add("APP_PORT", () -> "8080");
        registry.add("APP_DEFAULT_TAX_RATE", () -> "0.00");
        registry.add("APP_DEFAULT_SHIPPING_COST", () -> "0.00");
        registry.add("APP_SUPPORTED_CURRENCIES", () -> "USD,EUR,GBP");
        registry.add("app.name", () -> "Shopping Cart API");
        registry.add("app.default-tax-rate", () -> "0.00");
        registry.add("app.default-shipping-cost", () -> "0.00");
        registry.add("app.supported-currencies", () -> "USD,EUR,GBP");
    }
}
