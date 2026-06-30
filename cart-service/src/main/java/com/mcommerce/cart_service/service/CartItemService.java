package com.mcommerce.cart_service.service;

import com.mcommerce.cart_service.entity.Cart;

public interface CartItemService {

    /**
     * Ajouter un produit au panier
     */
    void addItem(Cart cart, Long productId);

    /**
     * Modifier la quantité d'un produit
     */
    void updateQuantity(Cart cart, Long productId, int quantity);

    /**
     * Supprimer un produit du panier
     */
    void removeItem(Cart cart, Long productId);
}
