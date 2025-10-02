/**
 * App: Shopping Cart API
 * Package: com.example.app.web
 * File: MetaController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-02T19:14:00Z
 * Exports: MetaController
 * Description: Exposes metadata endpoints that surface runtime configuration details for
 *              the Shopping Cart API service.
 */
package com.example.app.web;

import com.example.app.config.AppProperties;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that returns environment metadata describing the application identity and
 * listening port for diagnostic purposes.
 */
@RestController
@RequestMapping("/meta")
public class MetaController {

  private final AppProperties props;

  /**
   * Creates a new metadata controller backed by the supplied application properties.
   *
   * @param props validated application configuration properties
   */
  public MetaController(AppProperties props) {
    this.props = props;
  }

  /**
   * Reports the configured application name and port.
   *
   * @return immutable key-value pairs describing the current environment
   */
  @GetMapping("/env")
  public Map<String, Object> env() {
    return Map.of("app", props.getName(), "port", props.getPort());
  }
}
