package com.mcommerce.cart_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Panier auquel appartient l'item
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    /**
     * Snapshot du produit (provenant de product-service)
     */
    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    private String productImageUrl;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int quantity;

    /**
     * Total de cet item = prix * quantité
     */
    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    @PrePersist
    @PreUpdate
    public void validateQuantity() {
        if (quantity <= 0) {
            quantity = 1;
        }
    }
}
