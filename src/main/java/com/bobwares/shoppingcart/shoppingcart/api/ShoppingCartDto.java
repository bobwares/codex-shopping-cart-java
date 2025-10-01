/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.shoppingcart.api
 * File: ShoppingCartDto.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-10-01T19:32:32Z
 * Exports: ShoppingCartDto
 * Description: DTO definitions for shopping cart REST endpoints, including validation and OpenAPI annotations.
 */
package com.bobwares.shoppingcart.shoppingcart.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public final class ShoppingCartDto {

    private ShoppingCartDto() {
    }

    @Schema(name = "ShoppingCartItem")
    public static class Item {

        @Schema(description = "Unique identifier for the product")
        @NotBlank
        private String productId;

        @Schema(description = "Display name of the product")
        @NotBlank
        private String name;

        @Schema(description = "Quantity of the product in the cart")
        @Positive
        private int quantity;

        @Schema(description = "Unit price of the product")
        @PositiveOrZero
        private BigDecimal unitPrice;

        @Schema(description = "Total price for the line item")
        @PositiveOrZero
        private BigDecimal totalPrice;

        @Schema(description = "Currency code in ISO 4217 format")
        @NotBlank
        @Size(min = 3, max = 3)
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

    @Schema(name = "ShoppingCartDiscount")
    public static class Discount {

        @Schema(description = "Discount or coupon code")
        @NotBlank
        private String code;

        @Schema(description = "Monetary amount of the discount")
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

    @Schema(name = "ShoppingCartCreateRequest")
    public static class CreateRequest {

        @Schema(description = "Unique identifier of the user that owns the cart", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        private UUID userId;

        @Schema(description = "Subtotal before adjustments")
        @PositiveOrZero
        private BigDecimal subtotal;

        @Schema(description = "Calculated tax amount")
        @PositiveOrZero
        private BigDecimal tax;

        @Schema(description = "Shipping charges applied to the cart")
        @PositiveOrZero
        private BigDecimal shipping;

        @Schema(description = "Grand total after tax, shipping, and discounts")
        @PositiveOrZero
        private BigDecimal total;

        @Schema(description = "Currency code in ISO 4217 format")
        @NotBlank
        @Size(min = 3, max = 3)
        private String currency;

        @Schema(description = "Line items contained in the cart")
        @Valid
        @NotEmpty
        private List<Item> items;

        @Schema(description = "Discount codes applied to the cart")
        @Valid
        private List<Discount> discounts;

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

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public List<Discount> getDiscounts() {
            return discounts;
        }

        public void setDiscounts(List<Discount> discounts) {
            this.discounts = discounts;
        }
    }

    @Schema(name = "ShoppingCartUpdateRequest")
    public static class UpdateRequest extends CreateRequest {
    }

    @Schema(name = "ShoppingCartResponse")
    public static class Response {

        @Schema(description = "Cart identifier")
        private UUID id;

        @Schema(description = "User identifier")
        private UUID userId;

        private BigDecimal subtotal;
        private BigDecimal tax;
        private BigDecimal shipping;
        private BigDecimal total;
        private String currency;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private List<Item> items;
        private List<Discount> discounts;

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

        public OffsetDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public OffsetDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public List<Discount> getDiscounts() {
            return discounts;
        }

        public void setDiscounts(List<Discount> discounts) {
            this.discounts = discounts;
        }
    }
}
