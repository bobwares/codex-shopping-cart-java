/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart
 * File: ShoppingCartDiscount.java
 * Version: 0.1.0
 * Turns: 2
 * Author: gpt-5-codex
 * Date: 2025-10-03T00:25:40Z
 * Exports: ShoppingCartDiscount
 * Description: Represents a discount applied to a shopping cart with amount tracking.
 */
package com.bobwares.shoppingcart;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

/**
 * Entity representing a discount row associated with a shopping cart.
 */
@Entity
@Table(
    name = "shopping_cart_discount",
    schema = "shopping_cart",
    uniqueConstraints = @UniqueConstraint(name = "uk_cart_discount_code", columnNames = {"shopping_cart_id", "code"})
)
public class ShoppingCartDiscount {

  @Id
  @GeneratedValue
  @UuidGenerator
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "discount_id", nullable = false, updatable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "shopping_cart_id", nullable = false)
  private ShoppingCart shoppingCart;

  @NotBlank
  @Size(max = 64)
  @Column(name = "code", nullable = false, length = 64)
  private String code;

  @NotNull
  @Column(name = "amount", nullable = false, precision = 12, scale = 2)
  private BigDecimal amount;

  public UUID getId() {
    return id;
  }

  public ShoppingCart getShoppingCart() {
    return shoppingCart;
  }

  public void setShoppingCart(ShoppingCart shoppingCart) {
    this.shoppingCart = shoppingCart;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppingCartDiscount that = (ShoppingCartDiscount) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
