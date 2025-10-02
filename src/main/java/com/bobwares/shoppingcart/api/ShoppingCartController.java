/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.api
 * File: ShoppingCartController.java
 * Version: 0.1.0
 * Turns: [1]
 * Author: Codex Agent <agent@local>
 * Date: 2025-10-02T10:47:14Z
 * Exports: ShoppingCartController
 * Description: REST controller exposing CRUD operations for shopping carts
 *              with OpenAPI documentation annotations.
 */
package com.bobwares.shoppingcart.api;

import com.bobwares.shoppingcart.ShoppingCart;
import com.bobwares.shoppingcart.ShoppingCartDiscount;
import com.bobwares.shoppingcart.ShoppingCartItem;
import com.bobwares.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
@Tag(name = "Shopping Carts", description = "Manage shopping cart resources")
public class ShoppingCartController {

    private final ShoppingCartService service;

    public ShoppingCartController(ShoppingCartService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(
        summary = "Create a shopping cart",
        responses = {
            @ApiResponse(responseCode = "201", description = "Cart created", content = @Content(schema = @Schema(implementation = ShoppingCartDto.Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid payload", content = @Content)
        }
    )
    public ResponseEntity<ShoppingCartDto.Response> create(@Valid @RequestBody ShoppingCartDto.CreateRequest request) {
        ShoppingCart cart = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(cart));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get a shopping cart by id",
        parameters = {
            @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "Cart identifier")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Cart found", content = @Content(schema = @Schema(implementation = ShoppingCartDto.Response.class))),
            @ApiResponse(responseCode = "404", description = "Cart not found", content = @Content)
        }
    )
    public ResponseEntity<ShoppingCartDto.Response> get(@PathVariable UUID id) {
        ShoppingCart cart = service.get(id);
        return ResponseEntity.ok(toResponse(cart));
    }

    @GetMapping
    @Operation(
        summary = "List shopping carts",
        parameters = {
            @Parameter(name = "userId", in = ParameterIn.QUERY, description = "Optional filter by user identifier")
        }
    )
    public ResponseEntity<List<ShoppingCartDto.Response>> list(@RequestParam(required = false) UUID userId) {
        List<ShoppingCart> carts = service.list(userId);
        List<ShoppingCartDto.Response> responses = carts.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update a shopping cart",
        parameters = {
            @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "Cart identifier")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Cart updated", content = @Content(schema = @Schema(implementation = ShoppingCartDto.Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid payload", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cart not found", content = @Content)
        }
    )
    public ResponseEntity<ShoppingCartDto.Response> update(@PathVariable UUID id, @Valid @RequestBody ShoppingCartDto.UpdateRequest request) {
        ShoppingCart cart = service.update(id, request);
        return ResponseEntity.ok(toResponse(cart));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a shopping cart",
        parameters = {
            @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "Cart identifier")
        },
        responses = {
            @ApiResponse(responseCode = "204", description = "Cart deleted"),
            @ApiResponse(responseCode = "404", description = "Cart not found", content = @Content)
        }
    )
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ShoppingCartDto.Response toResponse(ShoppingCart cart) {
        ShoppingCartDto.Response response = new ShoppingCartDto.Response();
        response.setId(cart.getId());
        response.setUserId(cart.getUserId());
        response.setSubtotal(cart.getSubtotal());
        response.setTax(cart.getTax());
        response.setShipping(cart.getShipping());
        response.setTotal(cart.getTotal());
        response.setCurrency(cart.getCurrency());
        response.setItems(cart.getItems().stream().map(this::toItemResponse).collect(Collectors.toList()));
        response.setDiscounts(cart.getDiscounts().stream().map(this::toDiscountResponse).collect(Collectors.toList()));
        response.setCreatedAt(cart.getCreatedAt());
        response.setUpdatedAt(cart.getUpdatedAt());
        return response;
    }

    private ShoppingCartDto.ItemResponse toItemResponse(ShoppingCartItem item) {
        ShoppingCartDto.ItemResponse response = new ShoppingCartDto.ItemResponse();
        response.setId(item.getId());
        response.setProductId(item.getProductId());
        response.setName(item.getName());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setTotalPrice(item.getTotalPrice());
        response.setCurrency(item.getCurrency());
        return response;
    }

    private ShoppingCartDto.DiscountResponse toDiscountResponse(ShoppingCartDiscount discount) {
        ShoppingCartDto.DiscountResponse response = new ShoppingCartDto.DiscountResponse();
        response.setId(discount.getId());
        response.setCode(discount.getCode());
        response.setAmount(discount.getAmount());
        return response;
    }
}
