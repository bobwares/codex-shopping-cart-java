/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.api
 * File: ShoppingCartDto.java
 * Version: 0.1.0
 * Turns: [1]
 * Author: Codex Agent <agent@local>
 * Date: 2025-10-02T10:47:14Z
 * Exports: ShoppingCartDto
 * Description: Data transfer objects for Shopping Cart API requests and
 *              responses, including validation and OpenAPI metadata.
 */
package com.bobwares.shoppingcart.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ShoppingCartDto {

    private ShoppingCartDto() {
    }

    @Schema(name = "ShoppingCartCreateRequest")
    public static class CreateRequest extends BaseCartRequest {
    }

    @Schema(name = "ShoppingCartUpdateRequest")
    public static class UpdateRequest extends BaseCartRequest {
    }

    public abstract static class BaseCartRequest {

        @Schema(description = "Owner of the shopping cart", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        private UUID userId;

        @Schema(description = "Currency code in ISO-4217 format", example = "USD", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        private String currency;

        @Schema(description = "Applied tax amount", example = "12.50")
        @NotNull
        @PositiveOrZero
        private BigDecimal tax = BigDecimal.ZERO;

        @Schema(description = "Shipping cost", example = "5.00")
        @NotNull
        @PositiveOrZero
        private BigDecimal shipping = BigDecimal.ZERO;

        @Schema(description = "Cart line items", requiredMode = Schema.RequiredMode.REQUIRED)
        @Valid
        @NotEmpty
        private List<ItemRequest> items = new ArrayList<>();

        @Schema(description = "Applied discounts")
        @Valid
        private List<DiscountRequest> discounts = new ArrayList<>();

        public UUID getUserId() {
            return userId;
        }

        public void setUserId(UUID userId) {
            this.userId = userId;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
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

        public List<ItemRequest> getItems() {
            return items;
        }

        public void setItems(List<ItemRequest> items) {
            this.items = items;
        }

        public List<DiscountRequest> getDiscounts() {
            return discounts;
        }

        public void setDiscounts(List<DiscountRequest> discounts) {
            this.discounts = discounts;
        }
    }

    public static class ItemRequest {

        @Schema(description = "Product identifier", example = "SKU-1001", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        private String productId;

        @Schema(description = "Product name", example = "Wireless Mouse", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        private String name;

        @Schema(description = "Quantity of the product", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(1)
        private int quantity;

        @Schema(description = "Unit price", example = "25.00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @PositiveOrZero
        private BigDecimal unitPrice;

        @Schema(description = "Currency code for the line item", example = "USD", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
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

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    public static class DiscountRequest {

        @Schema(description = "Discount code", example = "WELCOME10", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        private String code;

        @Schema(description = "Discount amount", example = "10.00", requiredMode = Schema.RequiredMode.REQUIRED)
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

    @Schema(name = "ShoppingCartResponse")
    public static class Response {

        private UUID id;
        private UUID userId;
        private BigDecimal subtotal;
        private BigDecimal tax;
        private BigDecimal shipping;
        private BigDecimal total;
        private String currency;
        private List<ItemResponse> items = new ArrayList<>();
        private List<DiscountResponse> discounts = new ArrayList<>();
        private Instant createdAt;
        private Instant updatedAt;

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

        public List<ItemResponse> getItems() {
            return items;
        }

        public void setItems(List<ItemResponse> items) {
            this.items = items;
        }

        public List<DiscountResponse> getDiscounts() {
            return discounts;
        }

        public void setDiscounts(List<DiscountResponse> discounts) {
            this.discounts = discounts;
        }

        public Instant getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
        }

        public Instant getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
        }
    }

    public static class ItemResponse {

        private UUID id;
        private String productId;
        private String name;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
        private String currency;

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
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

    public static class DiscountResponse {

        private UUID id;
        private String code;
        private BigDecimal amount;

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
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
    }
}
