package com.mcommerce.cart_service.repository;

import com.mcommerce.cart_service.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Trouver le panier d'un utilisateur
     */
    Optional<Cart> findByUserId(Long userId);

    /**
     * Vérifier si un panier existe déjà pour l'utilisateur
     */
    boolean existsByUserId(Long userId);
}
