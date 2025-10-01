/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.shoppingcart.domain.discount
 * File: ShoppingCartDiscount.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-10-01T19:32:32Z
 * Exports: ShoppingCartDiscount
 * Description: Represents a cart-level discount identified by a code and monetary amount.
 */
package com.bobwares.shoppingcart.shoppingcart.domain.discount;

import com.bobwares.shoppingcart.shoppingcart.domain.ShoppingCart;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "shopping_cart_discount", schema = "shopping_cart")
public class ShoppingCartDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private ShoppingCart cart;

    @Column(name = "code", nullable = false, length = 64)
    @NotBlank
    @Size(max = 64)
    private String code;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    @NotNull
    @PositiveOrZero
    private BigDecimal amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
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
