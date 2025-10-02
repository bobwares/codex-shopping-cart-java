/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart
 * File: ShoppingCart.java
 * Version: 0.1.0
 * Turns: [1]
 * Author: Codex Agent <agent@local>
 * Date: 2025-10-02T10:47:14Z
 * Exports: ShoppingCart
 * Description: JPA aggregate root representing a customer's shopping cart,
 *              including pricing, currency, and child line items/discounts.
 */
package com.bobwares.shoppingcart;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "shopping_cart", schema = "shopping_cart_app")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID userId;

    @Column(name = "subtotal", nullable = false, precision = 19, scale = 2)
    @NotNull
    @PositiveOrZero
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "tax", nullable = false, precision = 19, scale = 2)
    @NotNull
    @PositiveOrZero
    private BigDecimal tax = BigDecimal.ZERO;

    @Column(name = "shipping", nullable = false, precision = 19, scale = 2)
    @NotNull
    @PositiveOrZero
    private BigDecimal shipping = BigDecimal.ZERO;

    @Column(name = "total", nullable = false, precision = 19, scale = 2)
    @NotNull
    @PositiveOrZero
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public UUID getId() {
        return id;
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

    public List<ShoppingCartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void setItems(List<ShoppingCartItem> newItems) {
        this.items.clear();
        if (newItems != null) {
            newItems.forEach(this::addItem);
        }
    }

    public void addItem(ShoppingCartItem item) {
        if (item == null) {
            return;
        }
        item.setShoppingCart(this);
        this.items.add(item);
    }

    public void clearItems() {
        this.items.clear();
    }

    public List<ShoppingCartDiscount> getDiscounts() {
        return Collections.unmodifiableList(discounts);
    }

    public void setDiscounts(List<ShoppingCartDiscount> newDiscounts) {
        this.discounts.clear();
        if (newDiscounts != null) {
            newDiscounts.forEach(this::addDiscount);
        }
    }

    public void addDiscount(ShoppingCartDiscount discount) {
        if (discount == null) {
            return;
        }
        discount.setShoppingCart(this);
        this.discounts.add(discount);
    }

    public void clearDiscounts() {
        this.discounts.clear();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
