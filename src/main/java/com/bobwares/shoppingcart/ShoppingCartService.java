/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart
 * File: ShoppingCartService.java
 * Version: 0.1.0
 * Turns: 2
 * Author: gpt-5-codex
 * Date: 2025-10-03T00:25:40Z
 * Exports: ShoppingCartService
 * Description: Provides transactional CRUD operations and mapping between entities and API DTOs.
 */
package com.bobwares.shoppingcart;

import com.bobwares.shoppingcart.api.ShoppingCartDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Service that manages {@link ShoppingCart} aggregates and coordinates persistence operations.
 */
@Service
@Transactional
public class ShoppingCartService {

  private final ShoppingCartRepository repository;

  public ShoppingCartService(ShoppingCartRepository repository) {
    this.repository = repository;
  }

  /**
   * Creates a new shopping cart for a user and persists it.
   *
   * @param request validated create payload
   * @return persisted shopping cart response
   */
  public ShoppingCartDto.Response create(ShoppingCartDto.CreateRequest request) {
    if (repository.existsByUserId(request.getUserId())) {
      throw new IllegalArgumentException("User already has an active shopping cart");
    }

    ShoppingCart cart = new ShoppingCart();
    cart.setId(UUID.randomUUID());
    cart.setUserId(request.getUserId());
    applyTotals(cart, request.getSubtotal(), request.getTax(), request.getShipping(), request.getTotal(), request.getCurrency());

    cart.replaceItems(request.getItems().stream().map(this::toItemEntity).collect(Collectors.toList()));
    cart.replaceDiscounts(request.getDiscounts().stream().map(this::toDiscountEntity).collect(Collectors.toList()));

    ShoppingCart saved = repository.save(cart);
    return mapToResponse(saved);
  }

  /**
   * Retrieves a shopping cart by its identifier.
   *
   * @param id cart identifier
   * @return DTO response
   */
  @Transactional(Transactional.TxType.SUPPORTS)
  public ShoppingCartDto.Response get(UUID id) {
    ShoppingCart cart = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found: " + id));
    return mapToResponse(cart);
  }

  /**
   * Lists all shopping carts.
   *
   * @return list of DTO responses
   */
  @Transactional(Transactional.TxType.SUPPORTS)
  public List<ShoppingCartDto.Response> list() {
    return repository.findAll().stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());
  }

  /**
   * Updates an existing shopping cart with provided totals and child collections.
   *
   * @param id cart identifier
   * @param request update payload
   * @return updated response
   */
  public ShoppingCartDto.Response update(UUID id, ShoppingCartDto.UpdateRequest request) {
    ShoppingCart cart = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found: " + id));

    applyTotals(cart, request.getSubtotal(), request.getTax(), request.getShipping(), request.getTotal(), request.getCurrency());
    cart.replaceItems(request.getItems().stream().map(this::toItemEntity).collect(Collectors.toList()));
    cart.replaceDiscounts(request.getDiscounts().stream().map(this::toDiscountEntity).collect(Collectors.toList()));

    ShoppingCart saved = repository.save(cart);
    return mapToResponse(saved);
  }

  /**
   * Deletes a shopping cart and associated rows.
   *
   * @param id cart identifier
   */
  public void delete(UUID id) {
    ShoppingCart cart = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found: " + id));
    repository.delete(cart);
  }

  private void applyTotals(ShoppingCart cart, BigDecimal subtotal, BigDecimal tax, BigDecimal shipping, BigDecimal total,
      String currency) {
    cart.setSubtotal(subtotal);
    cart.setTax(tax != null ? tax : BigDecimal.ZERO);
    cart.setShipping(shipping != null ? shipping : BigDecimal.ZERO);
    cart.setTotal(total);
    cart.setCurrency(currency != null ? currency.toUpperCase(Locale.ROOT) : null);
  }

  private ShoppingCartItem toItemEntity(ShoppingCartDto.ItemPayload payload) {
    ShoppingCartItem item = new ShoppingCartItem();
    item.setProductId(payload.getProductId());
    item.setName(payload.getName());
    item.setQuantity(payload.getQuantity());
    item.setUnitPrice(payload.getUnitPrice());
    item.setCurrency(payload.getCurrency().toUpperCase(Locale.ROOT));

    BigDecimal totalPrice = payload.getTotalPrice();
    if (totalPrice == null) {
      totalPrice = payload.getUnitPrice().multiply(BigDecimal.valueOf(payload.getQuantity()));
    }
    item.setTotalPrice(totalPrice);
    return item;
  }

  private ShoppingCartDiscount toDiscountEntity(ShoppingCartDto.DiscountPayload payload) {
    ShoppingCartDiscount discount = new ShoppingCartDiscount();
    discount.setCode(payload.getCode());
    discount.setAmount(payload.getAmount());
    return discount;
  }

  private ShoppingCartDto.Response mapToResponse(ShoppingCart cart) {
    List<ShoppingCartDto.Item> items = cart.getItems().stream()
        .map(item -> new ShoppingCartDto.Item(
            item.getId(),
            item.getProductId(),
            item.getName(),
            item.getQuantity(),
            item.getUnitPrice(),
            item.getTotalPrice(),
            item.getCurrency()
        ))
        .collect(Collectors.toList());

    List<ShoppingCartDto.Discount> discounts = cart.getDiscounts().stream()
        .map(discount -> new ShoppingCartDto.Discount(
            discount.getId(),
            discount.getCode(),
            discount.getAmount()
        ))
        .collect(Collectors.toList());

    return new ShoppingCartDto.Response(
        cart.getId(),
        cart.getUserId(),
        cart.getSubtotal(),
        cart.getTax(),
        cart.getShipping(),
        cart.getTotal(),
        cart.getCurrency(),
        cart.getCreatedAt(),
        cart.getUpdatedAt(),
        items,
        discounts
    );
  }
}
