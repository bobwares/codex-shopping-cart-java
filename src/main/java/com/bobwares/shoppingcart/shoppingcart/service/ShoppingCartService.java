/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.shoppingcart.service
 * File: ShoppingCartService.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-10-01T19:32:32Z
 * Exports: ShoppingCartService
 * Description: Provides transactional CRUD operations for shopping carts and performs DTO to entity mapping.
 */
package com.bobwares.shoppingcart.shoppingcart.service;

import com.bobwares.shoppingcart.shoppingcart.api.ShoppingCartDto;
import com.bobwares.shoppingcart.shoppingcart.domain.ShoppingCart;
import com.bobwares.shoppingcart.shoppingcart.domain.discount.ShoppingCartDiscount;
import com.bobwares.shoppingcart.shoppingcart.domain.item.ShoppingCartItem;
import com.bobwares.shoppingcart.shoppingcart.repository.ShoppingCartRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public ShoppingCartDto.Response create(ShoppingCartDto.CreateRequest request) {
        if (shoppingCartRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("A shopping cart already exists for this user.");
        }

        ShoppingCart cart = new ShoppingCart();
        applyRequestToEntity(cart, request);
        ShoppingCart saved = shoppingCartRepository.save(cart);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public ShoppingCartDto.Response get(UUID id) {
        ShoppingCart cart = shoppingCartRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found: " + id));
        return mapToResponse(cart);
    }

    @Transactional(readOnly = true)
    public List<ShoppingCartDto.Response> list() {
        return shoppingCartRepository
            .findAll()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public ShoppingCartDto.Response update(UUID id, ShoppingCartDto.UpdateRequest request) {
        ShoppingCart cart = shoppingCartRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found: " + id));

        if (!cart.getUserId().equals(request.getUserId())
            && shoppingCartRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("A shopping cart already exists for this user.");
        }

        applyRequestToEntity(cart, request);
        ShoppingCart saved = shoppingCartRepository.save(cart);
        return mapToResponse(saved);
    }

    public void delete(UUID id) {
        if (!shoppingCartRepository.existsById(id)) {
            throw new EntityNotFoundException("Shopping cart not found: " + id);
        }
        shoppingCartRepository.deleteById(id);
    }

    private void applyRequestToEntity(ShoppingCart cart, ShoppingCartDto.CreateRequest request) {
        cart.setUserId(request.getUserId());
        cart.setSubtotal(orZero(request.getSubtotal()));
        cart.setTax(orZero(request.getTax()));
        cart.setShipping(orZero(request.getShipping()));
        cart.setTotal(orZero(request.getTotal()));
        cart.setCurrency(request.getCurrency());

        cart.clearItems();
        List<ShoppingCartDto.Item> itemDtos = request.getItems();
        if (itemDtos != null) {
            for (ShoppingCartDto.Item dto : itemDtos) {
                ShoppingCartItem item = new ShoppingCartItem();
                item.setProductId(dto.getProductId());
                item.setName(dto.getName());
                item.setQuantity(dto.getQuantity());
                item.setUnitPrice(orZero(dto.getUnitPrice()));
                item.setTotalPrice(orZero(dto.getTotalPrice()));
                item.setCurrency(dto.getCurrency());
                cart.addItem(item);
            }
        }

        cart.clearDiscounts();
        List<ShoppingCartDto.Discount> discountDtos = request.getDiscounts();
        if (discountDtos != null) {
            for (ShoppingCartDto.Discount dto : discountDtos) {
                ShoppingCartDiscount discount = new ShoppingCartDiscount();
                discount.setCode(dto.getCode());
                discount.setAmount(orZero(dto.getAmount()));
                cart.addDiscount(discount);
            }
        }
    }

    private ShoppingCartDto.Response mapToResponse(ShoppingCart cart) {
        ShoppingCartDto.Response response = new ShoppingCartDto.Response();
        response.setId(cart.getId());
        response.setUserId(cart.getUserId());
        response.setSubtotal(cart.getSubtotal());
        response.setTax(cart.getTax());
        response.setShipping(cart.getShipping());
        response.setTotal(cart.getTotal());
        response.setCurrency(cart.getCurrency());
        response.setCreatedAt(cart.getCreatedAt());
        response.setUpdatedAt(cart.getUpdatedAt());
        response.setItems(cart.getItems().stream().map(this::mapItem).collect(Collectors.toList()));
        response.setDiscounts(cart.getDiscounts().stream().map(this::mapDiscount).collect(Collectors.toList()));
        return response;
    }

    private ShoppingCartDto.Item mapItem(ShoppingCartItem entity) {
        ShoppingCartDto.Item item = new ShoppingCartDto.Item();
        item.setProductId(entity.getProductId());
        item.setName(entity.getName());
        item.setQuantity(entity.getQuantity());
        item.setUnitPrice(entity.getUnitPrice());
        item.setTotalPrice(entity.getTotalPrice());
        item.setCurrency(entity.getCurrency());
        return item;
    }

    private ShoppingCartDto.Discount mapDiscount(ShoppingCartDiscount entity) {
        ShoppingCartDto.Discount discount = new ShoppingCartDto.Discount();
        discount.setCode(entity.getCode());
        discount.setAmount(entity.getAmount());
        return discount;
    }

    private BigDecimal orZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
