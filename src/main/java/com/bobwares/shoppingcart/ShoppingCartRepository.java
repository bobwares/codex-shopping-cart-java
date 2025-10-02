/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart
 * File: ShoppingCartRepository.java
 * Version: 0.1.0
 * Turns: [1]
 * Author: Codex Agent <agent@local>
 * Date: 2025-10-02T10:47:14Z
 * Exports: ShoppingCartRepository
 * Description: Spring Data repository for persisting and querying shopping
 *              carts and their aggregates.
 */
package com.bobwares.shoppingcart;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {

    List<ShoppingCart> findByUserId(UUID userId);

    Optional<ShoppingCart> findByIdAndUserId(UUID id, UUID userId);
}
