/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart
 * File: Application.java
 * Version: 0.1.0
 * Turns: 1
 * Author: gpt-5-codex
 * Date: 2025-10-02T23:02:16Z
 * Exports: Application
 * Description: Boots the Spring application context for the Shopping Cart API.
 *              Method main: Starts the Spring Boot runtime using the provided arguments.
 */
package com.bobwares.shoppingcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Entry point that launches the Spring Boot application and enables configuration property scanning.
 */
@SpringBootApplication
@ConfigurationPropertiesScan("com.bobwares.shoppingcart.config")
public class Application {

  /**
   * Starts the Spring Boot application.
   *
   * @param args runtime arguments forwarded to the Spring Boot launcher
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
