package com.mcommerce.product_service.repository;

import com.mcommerce.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Produits actifs uniquement
    Page<Product> findByActiveTrue(Pageable pageable);

    // Recherche par nom + actifs uniquement
    Page<Product> findByNameContainingIgnoreCaseAndActiveTrue(
            String name,
            Pageable pageable
    );
}
