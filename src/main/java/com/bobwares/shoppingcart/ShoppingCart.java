/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart
 * File: ShoppingCart.java
 * Version: 0.1.0
 * Turns: 2
 * Author: gpt-5-codex
 * Date: 2025-10-03T00:25:40Z
 * Exports: ShoppingCart
 * Description: JPA aggregate root representing a shopping cart with pricing totals, currency, and child collections.
 */
package com.bobwares.shoppingcart;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

/**
 * Aggregate root entity for shopping carts. Captures cart pricing totals and relations to cart items and discounts.
 */
@Entity
@Table(
    name = "shopping_cart",
    schema = "shopping_cart",
    uniqueConstraints = @UniqueConstraint(name = "uk_shopping_cart_user_id", columnNames = "user_id")
)
public class ShoppingCart {

  @Id
  @Column(name = "shopping_cart_id", nullable = false, updatable = false)
  @JdbcTypeCode(SqlTypes.UUID)
  private UUID id;

  @NotNull
  @Column(name = "user_id", nullable = false, unique = true)
  @JdbcTypeCode(SqlTypes.UUID)
  private UUID userId;

  @NotNull
  @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
  private BigDecimal subtotal;

  @Column(name = "tax", precision = 12, scale = 2)
  private BigDecimal tax;

  @Column(name = "shipping", precision = 12, scale = 2)
  private BigDecimal shipping;

  @NotNull
  @Column(name = "total", nullable = false, precision = 12, scale = 2)
  private BigDecimal total;

  @NotBlank
  @Size(min = 3, max = 3)
  @Pattern(regexp = "^[A-Z]{3}$")
  @Column(name = "currency", nullable = false, length = 3)
  private String currency;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @OneToMany(
      mappedBy = "shoppingCart",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private final List<ShoppingCartItem> items = new ArrayList<>();

  @OneToMany(
      mappedBy = "shoppingCart",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private final List<ShoppingCartDiscount> discounts = new ArrayList<>();

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public BigDecimal getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  public BigDecimal getShipping() {
    return shipping;
  }

  public void setShipping(BigDecimal shipping) {
    this.shipping = shipping;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public List<ShoppingCartItem> getItems() {
    return Collections.unmodifiableList(items);
  }

  public List<ShoppingCartDiscount> getDiscounts() {
    return Collections.unmodifiableList(discounts);
  }

  public void replaceItems(List<ShoppingCartItem> newItems) {
    items.clear();
    if (newItems != null) {
      newItems.forEach(this::addItem);
    }
  }

  public void replaceDiscounts(List<ShoppingCartDiscount> newDiscounts) {
    discounts.clear();
    if (newDiscounts != null) {
      newDiscounts.forEach(this::addDiscount);
    }
  }

  public void addItem(ShoppingCartItem item) {
    if (item != null) {
      item.setShoppingCart(this);
      items.add(item);
    }
  }

  public void addDiscount(ShoppingCartDiscount discount) {
    if (discount != null) {
      discount.setShoppingCart(this);
      discounts.add(discount);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppingCart that = (ShoppingCart) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
