package com.mcommerce.cart_service.repository;

import com.mcommerce.cart_service.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Trouver un item par panier + produit
     */
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}
