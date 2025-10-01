/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.shoppingcart.domain
 * File: ShoppingCart.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-10-01T19:32:32Z
 * Exports: ShoppingCart
 * Description: JPA aggregate root representing a shopping cart with monetary totals, currency, and related line items.
 */
package com.bobwares.shoppingcart.shoppingcart.domain;

import com.bobwares.shoppingcart.shoppingcart.domain.discount.ShoppingCartDiscount;
import com.bobwares.shoppingcart.shoppingcart.domain.item.ShoppingCartItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Entity
@Table(
    name = "shopping_cart",
    schema = "shopping_cart",
    uniqueConstraints = {
        @UniqueConstraint(name = "shopping_cart_user_unique", columnNames = "user_id")
    }
)
public class ShoppingCart {

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID userId;

    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal subtotal;

    @Column(name = "tax", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal tax;

    @Column(name = "shipping", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal shipping;

    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal total;

    @Column(name = "currency", nullable = false, length = 3)
    @NotBlank
    @Size(min = 3, max = 3)
    private String currency;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(
        mappedBy = "cart",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<ShoppingCartItem> items = new ArrayList<>();

    @OneToMany(
        mappedBy = "cart",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<ShoppingCartDiscount> discounts = new ArrayList<>();

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

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<ShoppingCartItem> getItems() {
        return items;
    }

    public void setItems(List<ShoppingCartItem> items) {
        this.items.clear();
        if (items != null) {
            items.forEach(this::addItem);
        }
    }

    public List<ShoppingCartDiscount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<ShoppingCartDiscount> discounts) {
        this.discounts.clear();
        if (discounts != null) {
            discounts.forEach(this::addDiscount);
        }
    }

    public void addItem(ShoppingCartItem item) {
        if (item == null) {
            return;
        }
        item.setCart(this);
        this.items.add(item);
    }

    public void addDiscount(ShoppingCartDiscount discount) {
        if (discount == null) {
            return;
        }
        discount.setCart(this);
        this.discounts.add(discount);
    }

    public void clearItems() {
        this.items.forEach(item -> item.setCart(null));
        this.items.clear();
    }

    public void clearDiscounts() {
        this.discounts.forEach(discount -> discount.setCart(null));
        this.discounts.clear();
    }
}
