package com.mcommerce.cart_service.service;

import com.mcommerce.cart_service.dto.CartResponse;

public interface CartService {

    /**
     * Récupérer le panier d'un utilisateur
     */
    CartResponse getCartByUserId(Long userId);

    /**
     * Ajouter un produit au panier
     */
    CartResponse addProductToCart(Long userId, Long productId);

    /**
     * Modifier la quantité d'un produit
     */
    CartResponse updateProductQuantity(Long userId, Long productId, int quantity);

    /**
     * Supprimer un produit du panier
     */
    CartResponse removeProductFromCart(Long userId, Long productId);

    /**
     * Vider le panier
     * @param userId
     */
    public void clearCart(Long userId);
}