/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.shoppingcart
 * File: ShoppingCartApiApplication.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-10-01T19:32:32Z
 * Exports: ShoppingCartApiApplication
 * Description: Spring Boot entry point that bootstraps the Shopping Cart API and registers configuration properties.
 */
package com.bobwares.shoppingcart.shoppingcart;

import com.bobwares.shoppingcart.shoppingcart.config.AppProperties;
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
