/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.api
 * File: ShoppingCartControllerIT.java
 * Version: 0.1.0
 * Turns: 2
 * Author: gpt-5-codex
 * Date: 2025-10-03T00:25:40Z
 * Exports: ShoppingCartControllerIT
 * Description: Integration tests exercising shopping cart CRUD endpoints against PostgreSQL via Testcontainers.
 */
package com.bobwares.shoppingcart.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Integration tests for {@link ShoppingCartController} using Testcontainers-backed PostgreSQL.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class ShoppingCartControllerIT {

  @Container
  private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16");

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @DynamicPropertySource
  static void registerProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    registry.add("spring.jpa.properties.hibernate.default_schema", () -> "shopping_cart");
    registry.add("spring.datasource.hikari.maximum-pool-size", () -> "2");
  }

  @Test
  void shouldExecuteCrudFlow() throws Exception {
    UUID userId = UUID.randomUUID();
    Map<String, Object> createPayload = Map.of(
        "userId", userId,
        "subtotal", BigDecimal.valueOf(120.00),
        "tax", BigDecimal.valueOf(10.00),
        "shipping", BigDecimal.valueOf(5.00),
        "total", BigDecimal.valueOf(135.00),
        "currency", "USD",
        "items", List.of(Map.of(
            "productId", "SKU-100",
            "name", "Mechanical Keyboard",
            "quantity", 1,
            "unitPrice", BigDecimal.valueOf(120.00),
            "totalPrice", BigDecimal.valueOf(120.00),
            "currency", "USD"
        )),
        "discounts", List.of(Map.of(
            "code", "SAVE5",
            "amount", BigDecimal.valueOf(5.00)
        ))
    );

    MvcResult createResult = mockMvc.perform(post("/api/shopping-carts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createPayload)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andReturn();

    JsonNode createNode = objectMapper.readTree(createResult.getResponse().getContentAsString());
    String cartId = createNode.get("id").asText();
    assertThat(cartId).isNotBlank();

    mockMvc.perform(get("/api/shopping-carts/{id}", cartId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(userId.toString()));

    Map<String, Object> updatePayload = Map.of(
        "subtotal", BigDecimal.valueOf(110.00),
        "tax", BigDecimal.valueOf(8.00),
        "shipping", BigDecimal.valueOf(4.00),
        "total", BigDecimal.valueOf(122.00),
        "currency", "USD",
        "items", List.of(Map.of(
            "productId", "SKU-101",
            "name", "Wireless Mouse",
            "quantity", 2,
            "unitPrice", BigDecimal.valueOf(40.00),
            "totalPrice", BigDecimal.valueOf(80.00),
            "currency", "USD"
        )),
        "discounts", List.of()
    );

    mockMvc.perform(put("/api/shopping-carts/{id}", cartId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatePayload)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.items[0].productId").value("SKU-101"));

    mockMvc.perform(delete("/api/shopping-carts/{id}", cartId))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/api/shopping-carts/{id}", cartId))
        .andExpect(status().isNotFound());
  }
}
