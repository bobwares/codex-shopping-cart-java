/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.shoppingcart.web
 * File: ShoppingCartControllerIT.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-10-01T19:32:32Z
 * Exports: ShoppingCartControllerIT
 * Description: Integration tests exercising the shopping cart REST endpoints with PostgreSQL Testcontainers.
 */
package com.bobwares.shoppingcart.shoppingcart.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bobwares.shoppingcart.shoppingcart.api.ShoppingCartDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class ShoppingCartControllerIT {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.default_schema", () -> "shopping_cart");
        registry.add("spring.jpa.properties.hibernate.hbm2ddl.create_namespaces", () -> "true");
        registry.add("spring.liquibase.enabled", () -> "false");
        registry.add("app.app-name", () -> "Shopping Cart API Test");
        registry.add("app.app-port", () -> "8080");
        registry.add("app.db-host", POSTGRES::getHost);
        registry.add("app.db-port", () -> String.valueOf(POSTGRES.getMappedPort(5432)));
        registry.add("app.db-name", POSTGRES::getDatabaseName);
        registry.add("app.db-username", POSTGRES::getUsername);
        registry.add("app.db-password", POSTGRES::getPassword);
        registry.add("app.db-schema", () -> "shopping_cart");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateReadUpdateAndDeleteCart() throws Exception {
        ShoppingCartDto.CreateRequest createRequest = buildCreateRequest();
        String createPayload = objectMapper.writeValueAsString(createRequest);

        String createResponse = mockMvc.perform(post("/api/shopping-carts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        JsonNode createdNode = objectMapper.readTree(createResponse);
        String id = createdNode.get("id").asText();
        assertThat(id).isNotBlank();

        mockMvc.perform(get("/api/shopping-carts/" + id))
            .andExpect(status().isOk());

        ShoppingCartDto.UpdateRequest updateRequest = new ShoppingCartDto.UpdateRequest();
        updateRequest.setUserId(createRequest.getUserId());
        updateRequest.setSubtotal(BigDecimal.valueOf(125));
        updateRequest.setTax(BigDecimal.valueOf(10));
        updateRequest.setShipping(BigDecimal.valueOf(5));
        updateRequest.setTotal(BigDecimal.valueOf(140));
        updateRequest.setCurrency("USD");
        updateRequest.setItems(createRequest.getItems());
        updateRequest.setDiscounts(createRequest.getDiscounts());

        mockMvc.perform(put("/api/shopping-carts/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk());

        mockMvc.perform(delete("/api/shopping-carts/" + id))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/shopping-carts/" + id))
            .andExpect(status().isNotFound());
    }

    private ShoppingCartDto.CreateRequest buildCreateRequest() {
        ShoppingCartDto.CreateRequest request = new ShoppingCartDto.CreateRequest();
        request.setUserId(UUID.randomUUID());
        request.setSubtotal(BigDecimal.valueOf(120));
        request.setTax(BigDecimal.valueOf(9));
        request.setShipping(BigDecimal.valueOf(5));
        request.setTotal(BigDecimal.valueOf(134));
        request.setCurrency("USD");

        ShoppingCartDto.Item item = new ShoppingCartDto.Item();
        item.setProductId("SKU-2001");
        item.setName("Integration Test Item");
        item.setQuantity(2);
        item.setUnitPrice(BigDecimal.valueOf(60));
        item.setTotalPrice(BigDecimal.valueOf(120));
        item.setCurrency("USD");

        request.setItems(List.of(item));
        request.setDiscounts(List.of());
        return request;
    }
}
