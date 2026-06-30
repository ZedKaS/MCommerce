package com.mcommerce.cart_service.web;

import com.mcommerce.cart_service.dto.CartResponse;
import com.mcommerce.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * Voir MON panier (userId depuis JWT via Gateway)
     */
    @GetMapping("/me")
    public ResponseEntity<CartResponse> getMyCart(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return ResponseEntity.ok(
                cartService.getCartByUserId(userId)
        );
    }

    /**
     * Ajouter un produit à MON panier
     */
    @PostMapping("/me/items/{productId}")
    public ResponseEntity<CartResponse> addProductToMyCart(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                cartService.addProductToCart(userId, productId)
        );
    }

    /**
     * Modifier la quantité d'un produit de MON panier
     */
    @PutMapping("/me/items/{productId}")
    public ResponseEntity<CartResponse> updateMyProductQuantity(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId,
            @RequestParam int quantity
    ) {
        return ResponseEntity.ok(
                cartService.updateProductQuantity(userId, productId, quantity)
        );
    }

    /**
     * Supprimer un produit de MON panier
     */
    @DeleteMapping("/me/items/{productId}")
    public ResponseEntity<CartResponse> removeProductFromMyCart(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                cartService.removeProductFromCart(userId, productId)
        );
    }

    /**
     * Vider complètement MON panier
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> clearMyCart(
            @RequestHeader("X-User-Id") Long userId
    ) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
