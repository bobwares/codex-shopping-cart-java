/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart
 * File: ApplicationSmokeTest.java
 * Version: 0.2.0
 * Turns: 1, 2
 * Author: gpt-5-codex
 * Date: 2025-10-03T00:34:56Z
 * Exports: ApplicationSmokeTest
 * Description: Verifies that the Spring Boot application context loads successfully.
 *              Method contextLoads: Triggers the default Spring Boot context startup for validation.
 */
package com.bobwares.shoppingcart;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Smoke test ensuring the application context loads without throwing exceptions.
 */
@SpringBootTest(properties = {
    "spring.liquibase.enabled=false",
    "spring.datasource.url=jdbc:h2:mem:shopping_cart_smoke;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=none"
})
class ApplicationSmokeTest {

  /**
   * Executes a no-op test to confirm the Spring context can start.
   */
  @Test
  void contextLoads() {
    // Intentionally empty - the test passes if the context initializes.
  }
}
