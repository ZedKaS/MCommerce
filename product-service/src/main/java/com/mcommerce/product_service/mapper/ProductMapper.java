package com.mcommerce.product_service.mapper;

import com.mcommerce.product_service.dto.ProductDto;
import com.mcommerce.product_service.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    // Entity ➜ DTO
    public ProductDto toDto(Product product) {
        if(product == null) return null;
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .active(product.isActive())
                .createdDate(product.getCreatedDate())
                .updatedDate(product.getUpdatedDate())
                .build();
    }

    // DTO ➜ Entity
    public Product toEntity(ProductDto dto) {
        if(dto == null) return null;
        return Product.builder()
                .id(dto.getId()) // utile pour update
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .active(dto.isActive())
                .createdDate(dto.getCreatedDate())
                .updatedDate(dto.getUpdatedDate())
                .build();
    }
}
