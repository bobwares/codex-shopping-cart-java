/**
 * App: Shopping Cart API
 * Package: com.example.app
 * File: Application.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-02T19:14:00Z
 * Exports: Application
 * Description: Bootstraps the Shopping Cart API Spring Boot application and configures
 *              component scanning for configuration properties.
 */
package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Primary application entry point that starts the Spring Boot runtime and scans the
 * configuration package for `@ConfigurationProperties` beans.
 */
@SpringBootApplication
@ConfigurationPropertiesScan("com.example.app.config")
public class Application {

  /**
   * Launches the Spring Boot application using the supplied command-line arguments.
   *
   * @param args command-line arguments passed to the JVM
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
