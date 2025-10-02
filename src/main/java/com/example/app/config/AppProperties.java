/**
 * App: Shopping Cart API
 * Package: com.example.app.config
 * File: AppProperties.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-02T19:14:00Z
 * Exports: AppProperties
 * Description: Declares strongly typed application configuration properties with
 *              validation constraints for environment-driven settings.
 */
package com.example.app.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties bound to the {@code app} prefix to expose validated metadata
 * for the application name and HTTP port.
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app")
public class AppProperties {

  /**
   * Human-friendly name for the backend service exposed via the metadata endpoint.
   */
  @NotBlank
  private String name = "backend";

  /**
   * Port on which the application listens; constrained to the valid TCP port range.
   */
  @NotNull
  @Min(1)
  @Max(65535)
  private Integer port = 8080;
}
