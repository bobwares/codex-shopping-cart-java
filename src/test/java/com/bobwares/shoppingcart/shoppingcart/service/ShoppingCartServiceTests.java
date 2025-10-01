/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.shoppingcart.service
 * File: ShoppingCartServiceTests.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-10-01T19:32:32Z
 * Exports: ShoppingCartServiceTests
 * Description: Unit tests for ShoppingCartService covering happy paths and validation scenarios.
 */
package com.bobwares.shoppingcart.shoppingcart.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.bobwares.shoppingcart.shoppingcart.api.ShoppingCartDto;
import com.bobwares.shoppingcart.shoppingcart.domain.ShoppingCart;
import com.bobwares.shoppingcart.shoppingcart.repository.ShoppingCartRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTests {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    private ShoppingCartService shoppingCartService;

    @BeforeEach
    void setUp() {
        shoppingCartService = new ShoppingCartService(shoppingCartRepository);
    }

    @Test
    void createShouldPersistCart() {
        ShoppingCartDto.CreateRequest request = createRequest();
        ShoppingCart persisted = new ShoppingCart();
        persisted.setId(UUID.randomUUID());
        persisted.setUserId(request.getUserId());

        doReturn(persisted).when(shoppingCartRepository).save(any(ShoppingCart.class));

        ShoppingCartDto.Response response = shoppingCartService.create(request);

        assertThat(response.getUserId()).isEqualTo(request.getUserId());
        verify(shoppingCartRepository).save(any(ShoppingCart.class));
    }

    @Test
    void createShouldRejectDuplicateUser() {
        ShoppingCartDto.CreateRequest request = createRequest();
        doReturn(true).when(shoppingCartRepository).existsByUserId(request.getUserId());

        assertThatThrownBy(() -> shoppingCartService.create(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("already exists");
    }

    @Test
    void updateShouldPersistChanges() {
        ShoppingCartDto.UpdateRequest request = new ShoppingCartDto.UpdateRequest();
        request.setUserId(UUID.randomUUID());
        request.setCurrency("USD");
        request.setSubtotal(BigDecimal.TEN);
        request.setTax(BigDecimal.ONE);
        request.setShipping(BigDecimal.ZERO);
        request.setTotal(BigDecimal.TEN);
        request.setItems(createRequest().getItems());
        request.setDiscounts(createRequest().getDiscounts());

        ShoppingCart existing = new ShoppingCart();
        existing.setUserId(request.getUserId());

        doReturn(Optional.of(existing)).when(shoppingCartRepository).findById(any(UUID.class));
        doReturn(existing).when(shoppingCartRepository).save(any(ShoppingCart.class));

        ShoppingCartDto.Response response = shoppingCartService.update(UUID.randomUUID(), request);

        assertThat(response.getCurrency()).isEqualTo("USD");
        verify(shoppingCartRepository).save(existing);
    }

    @Test
    void getShouldThrowWhenMissing() {
        doReturn(Optional.empty()).when(shoppingCartRepository).findById(any(UUID.class));

        assertThatThrownBy(() -> shoppingCartService.get(UUID.randomUUID()))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteShouldThrowWhenMissing() {
        doReturn(false).when(shoppingCartRepository).existsById(any(UUID.class));

        assertThatThrownBy(() -> shoppingCartService.delete(UUID.randomUUID()))
            .isInstanceOf(EntityNotFoundException.class);
    }

    private ShoppingCartDto.CreateRequest createRequest() {
        ShoppingCartDto.CreateRequest request = new ShoppingCartDto.CreateRequest();
        request.setUserId(UUID.randomUUID());
        request.setCurrency("USD");
        request.setSubtotal(BigDecimal.valueOf(100));
        request.setTax(BigDecimal.valueOf(8));
        request.setShipping(BigDecimal.valueOf(5));
        request.setTotal(BigDecimal.valueOf(113));

        ShoppingCartDto.Item item = new ShoppingCartDto.Item();
        item.setProductId("SKU-1");
        item.setName("Sample");
        item.setQuantity(1);
        item.setUnitPrice(BigDecimal.valueOf(100));
        item.setTotalPrice(BigDecimal.valueOf(100));
        item.setCurrency("USD");

        ShoppingCartDto.Discount discount = new ShoppingCartDto.Discount();
        discount.setCode("WELCOME10");
        discount.setAmount(BigDecimal.TEN);

        request.setItems(List.of(item));
        request.setDiscounts(List.of(discount));
        return request;
    }
}
