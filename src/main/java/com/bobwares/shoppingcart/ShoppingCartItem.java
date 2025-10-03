/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart
 * File: ShoppingCartItem.java
 * Version: 0.1.0
 * Turns: 2
 * Author: gpt-5-codex
 * Date: 2025-10-03T00:25:40Z
 * Exports: ShoppingCartItem
 * Description: Represents a line item within a shopping cart including product and pricing metadata.
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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

/**
 * Entity representing a cart line item.
 */
@Entity
@Table(
    name = "shopping_cart_item",
    schema = "shopping_cart",
    uniqueConstraints = @UniqueConstraint(name = "uk_cart_item_cart_product", columnNames = {"shopping_cart_id", "product_id"})
)
public class ShoppingCartItem {

  @Id
  @GeneratedValue
  @UuidGenerator
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "cart_item_id", nullable = false, updatable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "shopping_cart_id", nullable = false)
  private ShoppingCart shoppingCart;

  @NotBlank
  @Size(max = 64)
  @Column(name = "product_id", nullable = false, length = 64)
  private String productId;

  @NotBlank
  @Size(max = 255)
  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Min(1)
  @Column(name = "quantity", nullable = false)
  private int quantity;

  @NotNull
  @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
  private BigDecimal unitPrice;

  @NotBlank
  @Pattern(regexp = "^[A-Z]{3}$")
  @Size(min = 3, max = 3)
  @Column(name = "currency", nullable = false, length = 3)
  private String currency;

  @NotNull
  @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
  private BigDecimal totalPrice;

  public UUID getId() {
    return id;
  }

  public ShoppingCart getShoppingCart() {
    return shoppingCart;
  }

  public void setShoppingCart(ShoppingCart shoppingCart) {
    this.shoppingCart = shoppingCart;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppingCartItem that = (ShoppingCartItem) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
