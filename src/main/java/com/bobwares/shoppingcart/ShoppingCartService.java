/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart
 * File: ShoppingCartService.java
 * Version: 0.1.0
 * Turns: [1]
 * Author: Codex Agent <agent@local>
 * Date: 2025-10-02T10:47:14Z
 * Exports: ShoppingCartService
 * Description: Domain service for creating, retrieving, updating, and deleting
 *              shopping carts while enforcing pricing and currency rules.
 */
package com.bobwares.shoppingcart;

import com.bobwares.shoppingcart.api.ShoppingCartDto;
import com.bobwares.shoppingcart.config.AppProperties;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class ShoppingCartService {

    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final ShoppingCartRepository repository;
    private final AppProperties appProperties;

    public ShoppingCartService(ShoppingCartRepository repository, AppProperties appProperties) {
        this.repository = repository;
        this.appProperties = appProperties;
    }

    public ShoppingCart create(ShoppingCartDto.CreateRequest request) {
        ShoppingCart cart = new ShoppingCart();
        applyRequest(cart, request);
        return repository.save(cart);
    }

    @Transactional
    public ShoppingCart update(UUID id, ShoppingCartDto.UpdateRequest request) {
        ShoppingCart cart = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found: " + id));
        applyRequest(cart, request);
        return repository.save(cart);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Shopping cart not found: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public ShoppingCart get(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found: " + id));
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<ShoppingCart> list(UUID userId) {
        if (userId != null) {
            return repository.findByUserId(userId);
        }
        return repository.findAll();
    }

    private void applyRequest(ShoppingCart cart, ShoppingCartDto.BaseCartRequest request) {
        if (CollectionUtils.isEmpty(request.getItems())) {
            throw new IllegalArgumentException("Shopping cart must contain at least one item");
        }

        String normalizedCurrency = normalizeCurrency(request.getCurrency());
        validateCurrency(normalizedCurrency);

        cart.setUserId(request.getUserId());
        cart.setCurrency(normalizedCurrency);

        BigDecimal tax = defaultScale(request.getTax());
        BigDecimal shipping = defaultScale(request.getShipping());
        cart.setTax(tax);
        cart.setShipping(shipping);

        List<ShoppingCartItem> items = mapItems(request.getItems(), normalizedCurrency);
        cart.setItems(items);

        List<ShoppingCartDiscount> discounts = mapDiscounts(request.getDiscounts());
        cart.setDiscounts(discounts);

        BigDecimal subtotal = items.stream()
            .map(ShoppingCartItem::getTotalPrice)
            .reduce(ZERO, BigDecimal::add);

        BigDecimal discountTotal = discounts.stream()
            .map(ShoppingCartDiscount::getAmount)
            .reduce(ZERO, BigDecimal::add);

        BigDecimal total = subtotal.subtract(discountTotal).add(tax).add(shipping);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        cart.setSubtotal(subtotal);
        cart.setTotal(total);
    }

    private List<ShoppingCartItem> mapItems(List<ShoppingCartDto.ItemRequest> itemRequests, String cartCurrency) {
        if (itemRequests == null) {
            return Collections.emptyList();
        }
        List<ShoppingCartItem> items = new ArrayList<>(itemRequests.size());
        for (ShoppingCartDto.ItemRequest itemRequest : itemRequests) {
            String itemCurrency = normalizeCurrency(itemRequest.getCurrency());
            if (!cartCurrency.equals(itemCurrency)) {
                throw new IllegalArgumentException("Item currency must match cart currency");
            }
            ShoppingCartItem item = new ShoppingCartItem();
            item.setProductId(itemRequest.getProductId());
            item.setName(itemRequest.getName());
            item.setQuantity(itemRequest.getQuantity());

            BigDecimal unitPrice = defaultScale(itemRequest.getUnitPrice());
            item.setUnitPrice(unitPrice);

            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()))
                .setScale(2, RoundingMode.HALF_UP);
            item.setTotalPrice(totalPrice);
            item.setCurrency(itemCurrency);
            items.add(item);
        }
        return items;
    }

    private List<ShoppingCartDiscount> mapDiscounts(List<ShoppingCartDto.DiscountRequest> discountRequests) {
        if (discountRequests == null) {
            return Collections.emptyList();
        }
        List<ShoppingCartDiscount> discounts = new ArrayList<>(discountRequests.size());
        for (ShoppingCartDto.DiscountRequest discountRequest : discountRequests) {
            ShoppingCartDiscount discount = new ShoppingCartDiscount();
            discount.setCode(discountRequest.getCode().toUpperCase(Locale.ROOT));
            discount.setAmount(defaultScale(discountRequest.getAmount()));
            discounts.add(discount);
        }
        return discounts;
    }

    private void validateCurrency(String currency) {
        if (!appProperties.isCurrencySupported(currency)) {
            throw new IllegalArgumentException("Unsupported currency: " + currency);
        }
    }

    private String normalizeCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency is required");
        }
        return currency.toUpperCase(Locale.ROOT);
    }

    private BigDecimal defaultScale(BigDecimal value) {
        if (value == null) {
            return ZERO;
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
