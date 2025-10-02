/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart
 * File: ShoppingCartApiApplication.java
 * Version: 0.1.0
 * Turns: [1]
 * Author: Codex Agent <agent@local>
 * Date: 2025-10-02T10:47:14Z
 * Exports: ShoppingCartApiApplication
 * Description: Spring Boot entry point that enables configuration properties
 *              binding and component scanning for the Shopping Cart API.
 */
package com.bobwares.shoppingcart;

import com.bobwares.shoppingcart.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class ShoppingCartApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApiApplication.class, args);
    }
}
