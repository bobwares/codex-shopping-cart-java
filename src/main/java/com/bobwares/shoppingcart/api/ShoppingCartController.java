/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.api
 * File: ShoppingCartController.java
 * Version: 0.1.0
 * Turns: 2
 * Author: gpt-5-codex
 * Date: 2025-10-03T00:25:40Z
 * Exports: ShoppingCartController
 * Description: REST controller exposing CRUD endpoints for shopping carts with OpenAPI annotations.
 */
package com.bobwares.shoppingcart.api;

import com.bobwares.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller providing CRUD operations for shopping carts.
 */
@RestController
@RequestMapping("/api/shopping-carts")
@Tag(name = "Shopping Carts", description = "CRUD operations for shopping carts")
public class ShoppingCartController {

  private final ShoppingCartService shoppingCartService;

  public ShoppingCartController(ShoppingCartService shoppingCartService) {
    this.shoppingCartService = shoppingCartService;
  }

  @PostMapping
  @Operation(summary = "Create a shopping cart")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Cart created",
          content = @Content(schema = @Schema(implementation = ShoppingCartDto.Response.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
  })
  public ResponseEntity<ShoppingCartDto.Response> create(
      @Valid @org.springframework.web.bind.annotation.RequestBody
      @RequestBody(description = "Shopping cart creation payload", required = true)
      ShoppingCartDto.CreateRequest request) {
    ShoppingCartDto.Response response = shoppingCartService.create(request);
    return ResponseEntity.created(URI.create("/api/shopping-carts/" + response.id())).body(response);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Retrieve a shopping cart by id")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Cart found",
          content = @Content(schema = @Schema(implementation = ShoppingCartDto.Response.class))),
      @ApiResponse(responseCode = "404", description = "Cart not found", content = @Content)
  })
  public ResponseEntity<ShoppingCartDto.Response> get(@PathVariable UUID id) {
    return ResponseEntity.ok(shoppingCartService.get(id));
  }

  @GetMapping
  @Operation(summary = "List shopping carts")
  @ApiResponse(responseCode = "200", description = "List of carts",
      content = @Content(schema = @Schema(implementation = ShoppingCartDto.Response.class)))
  public ResponseEntity<List<ShoppingCartDto.Response>> list() {
    return ResponseEntity.ok(shoppingCartService.list());
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a shopping cart")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Cart updated",
          content = @Content(schema = @Schema(implementation = ShoppingCartDto.Response.class))),
      @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
      @ApiResponse(responseCode = "404", description = "Cart not found", content = @Content)
  })
  public ResponseEntity<ShoppingCartDto.Response> update(
      @PathVariable UUID id,
      @Valid @org.springframework.web.bind.annotation.RequestBody
      @RequestBody(description = "Shopping cart update payload", required = true)
      ShoppingCartDto.UpdateRequest request) {
    return ResponseEntity.ok(shoppingCartService.update(id, request));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a shopping cart")
  @ApiResponse(responseCode = "204", description = "Cart deleted")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    shoppingCartService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
