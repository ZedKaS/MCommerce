package com.mcommerce.product_service.service;

import com.mcommerce.product_service.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    /**
     * Creation d'un produit
     */
    ProductDto createProduct(ProductDto dto, MultipartFile image);


    /**
     * Recuperer un produit par son id
     */
    ProductDto getProductById(Long id);

    /**
     * Recuperer tous les produits actifs
     */
    Page<ProductDto> getProducts(
            String keyword,
            int page,
            int size,
            String sortBy,
            String direction
    );

    /**
     * Mettre a jours un produit
     */
    ProductDto updateProduct(Long id, ProductDto dto, MultipartFile image);


    /**
     * Supprimer un produit
     */
    void deleteProduct(Long id);


}
