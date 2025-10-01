/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.shoppingcart.repository
 * File: ShoppingCartRepository.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-10-01T19:32:32Z
 * Exports: ShoppingCartRepository
 * Description: Spring Data repository for shopping cart aggregates with convenience finders by user identifier.
 */
package com.bobwares.shoppingcart.shoppingcart.repository;

import com.bobwares.shoppingcart.shoppingcart.domain.ShoppingCart;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {

    Optional<ShoppingCart> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);
}
