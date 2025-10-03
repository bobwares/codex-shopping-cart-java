/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart
 * File: ShoppingCartServiceTests.java
 * Version: 0.1.0
 * Turns: 2
 * Author: gpt-5-codex
 * Date: 2025-10-03T00:25:40Z
 * Exports: ShoppingCartServiceTests
 * Description: Unit tests covering ShoppingCartService behavior and validation scenarios.
 */
package com.bobwares.shoppingcart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bobwares.shoppingcart.api.ShoppingCartDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link ShoppingCartService} using Mockito mocks.
 */
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
  void create_shouldPersistNewCart() {
    ShoppingCartDto.CreateRequest request = new ShoppingCartDto.CreateRequest();
    request.setUserId(UUID.randomUUID());
    request.setSubtotal(BigDecimal.valueOf(100));
    request.setTax(BigDecimal.TEN);
    request.setShipping(BigDecimal.valueOf(5));
    request.setTotal(BigDecimal.valueOf(115));
    request.setCurrency("usd");

    ShoppingCartDto.ItemPayload item = new ShoppingCartDto.ItemPayload();
    item.setProductId("SKU-1");
    item.setName("Keyboard");
    item.setQuantity(1);
    item.setUnitPrice(BigDecimal.valueOf(100));
    item.setCurrency("usd");
    request.setItems(List.of(item));

    request.setDiscounts(List.of());

    when(shoppingCartRepository.existsByUserId(request.getUserId())).thenReturn(false);
    when(shoppingCartRepository.save(any(ShoppingCart.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    ShoppingCartDto.Response response = shoppingCartService.create(request);

    assertThat(response.id()).isNotNull();
    assertThat(response.currency()).isEqualTo("USD");
    assertThat(response.items()).hasSize(1);

    ArgumentCaptor<ShoppingCart> captor = ArgumentCaptor.forClass(ShoppingCart.class);
    verify(shoppingCartRepository).save(captor.capture());
    ShoppingCart persisted = captor.getValue();
    assertThat(persisted.getUserId()).isEqualTo(request.getUserId());
    assertThat(persisted.getItems()).hasSize(1);
  }

  @Test
  void create_shouldRejectDuplicateUser() {
    ShoppingCartDto.CreateRequest request = new ShoppingCartDto.CreateRequest();
    request.setUserId(UUID.randomUUID());
    request.setSubtotal(BigDecimal.ONE);
    request.setTotal(BigDecimal.ONE);
    request.setCurrency("USD");
    ShoppingCartDto.ItemPayload item = new ShoppingCartDto.ItemPayload();
    item.setProductId("SKU");
    item.setName("Name");
    item.setQuantity(1);
    item.setUnitPrice(BigDecimal.ONE);
    item.setCurrency("USD");
    request.setItems(List.of(item));

    when(shoppingCartRepository.existsByUserId(request.getUserId())).thenReturn(true);

    assertThatThrownBy(() -> shoppingCartService.create(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("User already has an active shopping cart");
  }

  @Test
  void get_shouldReturnCart() {
    UUID cartId = UUID.randomUUID();
    ShoppingCart cart = buildCart(cartId);
    when(shoppingCartRepository.findById(cartId)).thenReturn(Optional.of(cart));

    ShoppingCartDto.Response response = shoppingCartService.get(cartId);

    assertThat(response.id()).isEqualTo(cartId);
    assertThat(response.items()).hasSize(1);
    assertThat(response.discounts()).hasSize(1);
  }

  @Test
  void delete_shouldRemoveCart() {
    UUID cartId = UUID.randomUUID();
    ShoppingCart cart = buildCart(cartId);
    when(shoppingCartRepository.findById(cartId)).thenReturn(Optional.of(cart));
    doNothing().when(shoppingCartRepository).delete(cart);

    shoppingCartService.delete(cartId);

    verify(shoppingCartRepository).delete(cart);
  }

  private ShoppingCart buildCart(UUID id) {
    ShoppingCart cart = new ShoppingCart();
    cart.setId(id);
    cart.setUserId(UUID.randomUUID());
    cart.setSubtotal(BigDecimal.valueOf(100));
    cart.setTax(BigDecimal.TEN);
    cart.setShipping(BigDecimal.ZERO);
    cart.setTotal(BigDecimal.valueOf(110));
    cart.setCurrency("USD");

    ShoppingCartItem item = new ShoppingCartItem();
    item.setProductId("SKU-1");
    item.setName("Keyboard");
    item.setQuantity(1);
    item.setUnitPrice(BigDecimal.valueOf(100));
    item.setCurrency("USD");
    item.setTotalPrice(BigDecimal.valueOf(100));
    cart.replaceItems(List.of(item));

    ShoppingCartDiscount discount = new ShoppingCartDiscount();
    discount.setCode("SAVE10");
    discount.setAmount(BigDecimal.TEN);
    cart.replaceDiscounts(List.of(discount));

    return cart;
  }
}
