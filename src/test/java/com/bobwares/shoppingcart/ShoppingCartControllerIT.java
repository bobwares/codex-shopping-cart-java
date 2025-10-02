/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart
 * File: ShoppingCartControllerIT.java
 * Version: 0.1.0
 * Turns: [1]
 * Author: Codex Agent <agent@local>
 * Date: 2025-10-02T10:47:14Z
 * Exports: ShoppingCartControllerIT
 * Description: Executes HTTP-level CRUD scenarios against the Shopping Cart
 *              controller using Testcontainers-backed PostgreSQL.
 */
package com.bobwares.shoppingcart;

import com.bobwares.shoppingcart.api.ShoppingCartDto;
import com.bobwares.shoppingcart.support.PostgresContainerSupport;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerIT extends PostgresContainerSupport {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ShoppingCartRepository repository;

    @Test
    void shoppingCartCrudFlow() {
        ShoppingCartDto.CreateRequest createRequest = new ShoppingCartDto.CreateRequest();
        UUID userId = UUID.fromString("e2d5a4a0-8fbc-4f33-8f76-6e7b3b5ea1f0");
        createRequest.setUserId(userId);
        createRequest.setCurrency("USD");
        createRequest.setTax(BigDecimal.valueOf(12.00));
        createRequest.setShipping(BigDecimal.valueOf(5.00));
        createRequest.setItems(List.of(
            buildItem("SKU-3001", "Studio Monitor", 1, BigDecimal.valueOf(150.00)),
            buildItem("SKU-3002", "Audio Interface", 1, BigDecimal.valueOf(110.00))
        ));
        createRequest.setDiscounts(List.of(buildDiscount("WELCOME", BigDecimal.valueOf(20.00))));

        ResponseEntity<ShoppingCartDto.Response> createdResponse = restTemplate.postForEntity("/api/carts", createRequest, ShoppingCartDto.Response.class);
        Assertions.assertEquals(201, createdResponse.getStatusCode().value());
        Assertions.assertNotNull(createdResponse.getBody());
        UUID cartId = createdResponse.getBody().getId();
        Assertions.assertNotNull(cartId);

        ResponseEntity<ShoppingCartDto.Response> fetchedResponse = restTemplate.getForEntity("/api/carts/{id}", ShoppingCartDto.Response.class, cartId);
        Assertions.assertEquals(200, fetchedResponse.getStatusCode().value());
        Assertions.assertEquals(cartId, fetchedResponse.getBody().getId());

        ResponseEntity<List<ShoppingCartDto.Response>> listResponse = restTemplate.exchange(
            "/api/carts?userId={userId}", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<ShoppingCartDto.Response>>() {}, userId);
        Assertions.assertEquals(200, listResponse.getStatusCode().value());
        Assertions.assertFalse(listResponse.getBody().isEmpty());

        ShoppingCartDto.UpdateRequest updateRequest = new ShoppingCartDto.UpdateRequest();
        updateRequest.setUserId(userId);
        updateRequest.setCurrency("USD");
        updateRequest.setTax(BigDecimal.valueOf(10.00));
        updateRequest.setShipping(BigDecimal.valueOf(8.00));
        updateRequest.setItems(List.of(
            buildItem("SKU-3001", "Studio Monitor", 1, BigDecimal.valueOf(150.00)),
            buildItem("SKU-3003", "Microphone", 2, BigDecimal.valueOf(90.00))
        ));
        updateRequest.setDiscounts(List.of(buildDiscount("LOYAL", BigDecimal.valueOf(15.00))));

        ResponseEntity<ShoppingCartDto.Response> updatedResponse = restTemplate.exchange(
            "/api/carts/{id}", HttpMethod.PUT, new HttpEntity<>(updateRequest), ShoppingCartDto.Response.class, cartId);
        Assertions.assertEquals(200, updatedResponse.getStatusCode().value());
        Assertions.assertEquals(2, updatedResponse.getBody().getItems().size());

        restTemplate.delete("/api/carts/{id}", cartId);
        Assertions.assertTrue(repository.findById(cartId).isEmpty());

        Assertions.assertThrows(HttpClientErrorException.NotFound.class,
            () -> restTemplate.getForEntity("/api/carts/{id}", ShoppingCartDto.Response.class, cartId));
    }

    private ShoppingCartDto.ItemRequest buildItem(String productId, String name, int quantity, BigDecimal unitPrice) {
        ShoppingCartDto.ItemRequest item = new ShoppingCartDto.ItemRequest();
        item.setProductId(productId);
        item.setName(name);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        item.setCurrency("USD");
        return item;
    }

    private ShoppingCartDto.DiscountRequest buildDiscount(String code, BigDecimal amount) {
        ShoppingCartDto.DiscountRequest discount = new ShoppingCartDto.DiscountRequest();
        discount.setCode(code);
        discount.setAmount(amount);
        return discount;
    }
}
