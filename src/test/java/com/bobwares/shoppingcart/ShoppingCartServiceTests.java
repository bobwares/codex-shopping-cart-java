/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart
 * File: ShoppingCartServiceTests.java
 * Version: 0.1.0
 * Turns: [1]
 * Author: Codex Agent <agent@local>
 * Date: 2025-10-02T10:47:14Z
 * Exports: ShoppingCartServiceTests
 * Description: Verifies shopping cart service behaviour including creation,
 *              updates, listing, and deletion with PostgreSQL persistence.
 */
package com.bobwares.shoppingcart;

import com.bobwares.shoppingcart.api.ShoppingCartDto;
import com.bobwares.shoppingcart.support.PostgresContainerSupport;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShoppingCartServiceTests extends PostgresContainerSupport {

    @Autowired
    private ShoppingCartService service;

    @Autowired
    private ShoppingCartRepository repository;

    @AfterEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void createPersistsCartAndComputesTotals() {
        ShoppingCartDto.CreateRequest request = buildCreateRequest();

        ShoppingCart cart = service.create(request);

        Assertions.assertNotNull(cart.getId(), "Cart ID should be generated");
        Assertions.assertEquals(BigDecimal.valueOf(175.00).setScale(2), cart.getSubtotal());
        Assertions.assertEquals(BigDecimal.valueOf(20.00).setScale(2), cart.getTax().add(cart.getShipping()));
        Assertions.assertEquals("USD", cart.getCurrency());
        Assertions.assertEquals(2, cart.getItems().size());
        Assertions.assertEquals(1, cart.getDiscounts().size());

        ShoppingCart persisted = repository.findById(cart.getId()).orElseThrow();
        Assertions.assertEquals(cart.getTotal(), persisted.getTotal());
    }

    @Test
    void updateReplacesItemsAndDiscounts() {
        ShoppingCart cart = service.create(buildCreateRequest());

        ShoppingCartDto.UpdateRequest updateRequest = new ShoppingCartDto.UpdateRequest();
        updateRequest.setUserId(cart.getUserId());
        updateRequest.setCurrency("USD");
        updateRequest.setTax(BigDecimal.valueOf(12.50));
        updateRequest.setShipping(BigDecimal.valueOf(7.00));
        updateRequest.setItems(List.of(createItem("SKU-2001", "Mechanical Keyboard", 1, BigDecimal.valueOf(120.00)),
            createItem("SKU-2002", "Wireless Mouse", 2, BigDecimal.valueOf(25.00))));
        updateRequest.setDiscounts(List.of(createDiscount("LOYALTY", BigDecimal.valueOf(5.00))));

        ShoppingCart updated = service.update(cart.getId(), updateRequest);

        Assertions.assertEquals(2, updated.getItems().size());
        Assertions.assertEquals(1, updated.getDiscounts().size());
        Assertions.assertEquals(BigDecimal.valueOf(184.50).setScale(2), updated.getTotal());
    }

    @Test
    void listFiltersByUserId() {
        ShoppingCartDto.CreateRequest request = buildCreateRequest();
        service.create(request);

        List<ShoppingCart> carts = service.list(request.getUserId());
        Assertions.assertEquals(1, carts.size());
        Assertions.assertEquals(request.getUserId(), carts.get(0).getUserId());
    }

    @Test
    void deleteRemovesCart() {
        ShoppingCart cart = service.create(buildCreateRequest());

        service.delete(cart.getId());

        Assertions.assertTrue(repository.findById(cart.getId()).isEmpty());
    }

    private ShoppingCartDto.CreateRequest buildCreateRequest() {
        ShoppingCartDto.CreateRequest request = new ShoppingCartDto.CreateRequest();
        request.setUserId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        request.setCurrency("USD");
        request.setTax(BigDecimal.valueOf(12.50));
        request.setShipping(BigDecimal.valueOf(7.50));
        request.setItems(List.of(
            createItem("SKU-1001", "Gaming Laptop", 1, BigDecimal.valueOf(150.00)),
            createItem("SKU-1002", "Gaming Mouse", 1, BigDecimal.valueOf(25.00))
        ));
        request.setDiscounts(List.of(createDiscount("WELCOME", BigDecimal.valueOf(10.00))));
        return request;
    }

    private ShoppingCartDto.ItemRequest createItem(String productId, String name, int quantity, BigDecimal unitPrice) {
        ShoppingCartDto.ItemRequest item = new ShoppingCartDto.ItemRequest();
        item.setProductId(productId);
        item.setName(name);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        item.setCurrency("USD");
        return item;
    }

    private ShoppingCartDto.DiscountRequest createDiscount(String code, BigDecimal amount) {
        ShoppingCartDto.DiscountRequest discount = new ShoppingCartDto.DiscountRequest();
        discount.setCode(code);
        discount.setAmount(amount);
        return discount;
    }
}
