/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.web
 * File: MetaController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: gpt-5-codex
 * Date: 2025-10-02T23:02:16Z
 * Exports: MetaController
 * Description: Exposes metadata endpoints for environment diagnostics.
 *              Method env: Returns basic application metadata for health verification.
 */
package com.bobwares.shoppingcart.web;

import com.bobwares.shoppingcart.config.AppProperties;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that returns environment information for operational visibility.
 */
@RestController
@RequestMapping("/meta")
public class MetaController {

  private final AppProperties props;

  /**
   * Creates the controller with validated application properties.
   *
   * @param props validated configuration backing the responses
   */
  public MetaController(AppProperties props) {
    this.props = props;
  }

  /**
   * Provides the configured application name and port for quick diagnostics.
   *
   * @return immutable map containing the application name and port
   */
  @GetMapping("/env")
  public Map<String, Object> env() {
    return Map.of("app", props.getName(), "port", props.getPort());
  }
}
