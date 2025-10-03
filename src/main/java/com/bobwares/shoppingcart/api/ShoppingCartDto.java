/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.api
 * File: ShoppingCartDto.java
 * Version: 0.1.0
 * Turns: 2
 * Author: gpt-5-codex
 * Date: 2025-10-03T00:25:40Z
 * Exports: ShoppingCartDto
 * Description: Defines API request and response payloads for Shopping Cart REST endpoints.
 */
package com.bobwares.shoppingcart.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Container for Shopping Cart API request and response DTOs.
 */
public final class ShoppingCartDto {

  private ShoppingCartDto() {
  }

  /**
   * Payload submitted when creating a shopping cart.
   */
  @Schema(description = "Request payload for creating a shopping cart")
  public static final class CreateRequest {

    @NotNull
    @Schema(description = "Identifier of the user that owns the cart", example = "d0fbb13a-7d5d-4d9a-9fc8-20a5c0dd768e")
    private UUID userId;

    @NotNull
    @PositiveOrZero
    @Schema(description = "Subtotal of cart items before taxes or discounts", example = "120.00")
    private BigDecimal subtotal;

    @PositiveOrZero
    @Schema(description = "Total tax applied to the cart", example = "9.20")
    private BigDecimal tax = BigDecimal.ZERO;

    @PositiveOrZero
    @Schema(description = "Shipping cost applied to the cart", example = "5.99")
    private BigDecimal shipping = BigDecimal.ZERO;

    @NotNull
    @PositiveOrZero
    @Schema(description = "Grand total for the cart", example = "135.19")
    private BigDecimal total;

    @NotBlank
    @Size(min = 3, max = 3)
    @Pattern(regexp = "^[A-Z]{3}$")
    @Schema(description = "Three-letter ISO currency code", example = "USD")
    private String currency;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<ItemPayload> items = new ArrayList<>();

    @Valid
    private List<DiscountPayload> discounts = new ArrayList<>();

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

    public List<ItemPayload> getItems() {
      return items;
    }

    public void setItems(List<ItemPayload> items) {
      this.items = items;
    }

    public List<DiscountPayload> getDiscounts() {
      return discounts;
    }

    public void setDiscounts(List<DiscountPayload> discounts) {
      this.discounts = discounts;
    }
  }

  /**
   * Payload submitted when updating a shopping cart.
   */
  @Schema(description = "Request payload for updating a shopping cart")
  public static final class UpdateRequest {

    @NotNull
    @PositiveOrZero
    @Schema(description = "Updated subtotal", example = "125.00")
    private BigDecimal subtotal;

    @PositiveOrZero
    @Schema(description = "Updated tax value", example = "10.00")
    private BigDecimal tax = BigDecimal.ZERO;

    @PositiveOrZero
    @Schema(description = "Updated shipping amount", example = "4.00")
    private BigDecimal shipping = BigDecimal.ZERO;

    @NotNull
    @PositiveOrZero
    @Schema(description = "Updated total", example = "139.00")
    private BigDecimal total;

    @NotBlank
    @Size(min = 3, max = 3)
    @Pattern(regexp = "^[A-Z]{3}$")
    @Schema(description = "Updated currency", example = "USD")
    private String currency;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<ItemPayload> items = new ArrayList<>();

    @Valid
    private List<DiscountPayload> discounts = new ArrayList<>();

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

    public List<ItemPayload> getItems() {
      return items;
    }

    public void setItems(List<ItemPayload> items) {
      this.items = items;
    }

    public List<DiscountPayload> getDiscounts() {
      return discounts;
    }

    public void setDiscounts(List<DiscountPayload> discounts) {
      this.discounts = discounts;
    }
  }

  /**
   * Response payload returned to API consumers.
   */
  @Schema(description = "Response envelope for shopping cart operations")
  public record Response(
      UUID id,
      UUID userId,
      BigDecimal subtotal,
      BigDecimal tax,
      BigDecimal shipping,
      BigDecimal total,
      String currency,
      Instant createdAt,
      Instant updatedAt,
      List<Item> items,
      List<Discount> discounts
  ) {
  }

  /**
   * Representation of a cart line item in API responses.
   */
  public record Item(
      UUID id,
      String productId,
      String name,
      int quantity,
      BigDecimal unitPrice,
      BigDecimal totalPrice,
      String currency
  ) {
  }

  /**
   * Representation of a discount entry in API responses.
   */
  public record Discount(
      UUID id,
      String code,
      BigDecimal amount
  ) {
  }

  /**
   * Payload definition for create/update item arrays.
   */
  public static final class ItemPayload {

    @NotBlank
    @Size(max = 64)
    private String productId;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    @Positive
    private Integer quantity;

    @NotNull
    @PositiveOrZero
    private BigDecimal unitPrice;

    @PositiveOrZero
    private BigDecimal totalPrice;

    @NotBlank
    @Size(min = 3, max = 3)
    @Pattern(regexp = "^[A-Z]{3}$")
    private String currency;

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

    public Integer getQuantity() {
      return quantity;
    }

    public void setQuantity(Integer quantity) {
      this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
      return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
      this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
      return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
      this.totalPrice = totalPrice;
    }

    public String getCurrency() {
      return currency;
    }

    public void setCurrency(String currency) {
      this.currency = currency;
    }
  }

  /**
   * Payload definition for create/update discount arrays.
   */
  public static final class DiscountPayload {

    @NotBlank
    @Size(max = 64)
    private String code;

    @NotNull
    @PositiveOrZero
    private BigDecimal amount;

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
  }
}
