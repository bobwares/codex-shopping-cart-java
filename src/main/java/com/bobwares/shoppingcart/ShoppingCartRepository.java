/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart
 * File: ShoppingCartRepository.java
 * Version: 0.1.0
 * Turns: 2
 * Author: gpt-5-codex
 * Date: 2025-10-03T00:25:40Z
 * Exports: ShoppingCartRepository
 * Description: Spring Data repository for the ShoppingCart aggregate with convenience lookups.
 */
package com.bobwares.shoppingcart;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository enabling CRUD and custom queries for {@link ShoppingCart} aggregates.
 */
@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {

  Optional<ShoppingCart> findByUserId(UUID userId);

  boolean existsByUserId(UUID userId);
}
