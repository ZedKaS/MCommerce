package com.mcommerce.product_service.controller;
import com.mcommerce.product_service.dto.ProductDto;
import com.mcommerce.product_service.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    /**
     * Créer un nouveau produit
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProductDto> createProduct(
            @RequestHeader("X-User-Role") String role,
            @RequestPart("product") @Valid ProductDto productDto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ProductDto createdProduct = productService.createProduct(productDto, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }



    /**
     * Recuperer tous les produits
     */
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(
                productService.getProducts(
                        keyword,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    /**
     * Recuperer un Produit
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Modifier un produit
     */
    @PatchMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProductDto> updateProduct(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id,
            @RequestPart("product") ProductDto productDto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ProductDto updatedProduct =
                productService.updateProduct(id, productDto, image);

        return ResponseEntity.ok(updatedProduct);
    }


    /**
     * Supprimer (désactiver) un produit
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id
    ) {
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }



}
