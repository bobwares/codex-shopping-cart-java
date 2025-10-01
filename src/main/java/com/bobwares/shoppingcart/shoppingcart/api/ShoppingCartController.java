/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.shoppingcart.api
 * File: ShoppingCartController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Codex Agent
 * Date: 2025-10-01T19:32:32Z
 * Exports: ShoppingCartController
 * Description: REST controller exposing CRUD endpoints for shopping carts with OpenAPI documentation.
 */
package com.bobwares.shoppingcart.shoppingcart.api;

import com.bobwares.shoppingcart.shoppingcart.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shopping-carts")
@Validated
@Tag(name = "Shopping Cart", description = "Manage shopping carts and their contents")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping
    @Operation(
        summary = "Create a shopping cart",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Cart payload",
            content = @Content(schema = @Schema(implementation = ShoppingCartDto.CreateRequest.class))
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Cart created", content = @Content(schema = @Schema(implementation = ShoppingCartDto.Response.class))),
            @ApiResponse(responseCode = "400", description = "Validation error")
        }
    )
    public ResponseEntity<ShoppingCartDto.Response> create(@Valid @RequestBody ShoppingCartDto.CreateRequest request) {
        ShoppingCartDto.Response response = shoppingCartService.create(request);
        return ResponseEntity.created(URI.create("/api/shopping-carts/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a cart by id", responses = {
        @ApiResponse(responseCode = "200", description = "Cart found", content = @Content(schema = @Schema(implementation = ShoppingCartDto.Response.class))),
        @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    public ResponseEntity<ShoppingCartDto.Response> get(@PathVariable UUID id) {
        return ResponseEntity.ok(shoppingCartService.get(id));
    }

    @GetMapping
    @Operation(summary = "List all carts")
    public ResponseEntity<List<ShoppingCartDto.Response>> list() {
        return ResponseEntity.ok(shoppingCartService.list());
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update a cart",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Updated cart payload",
            content = @Content(schema = @Schema(implementation = ShoppingCartDto.UpdateRequest.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Cart updated", content = @Content(schema = @Schema(implementation = ShoppingCartDto.Response.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
        }
    )
    public ResponseEntity<ShoppingCartDto.Response> update(
        @PathVariable UUID id,
        @Valid @RequestBody ShoppingCartDto.UpdateRequest request
    ) {
        return ResponseEntity.ok(shoppingCartService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a cart", responses = {
        @ApiResponse(responseCode = "204", description = "Cart deleted"),
        @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        shoppingCartService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
