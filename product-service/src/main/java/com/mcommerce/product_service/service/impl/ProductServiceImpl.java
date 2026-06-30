package com.mcommerce.product_service.service.impl;

import com.mcommerce.product_service.dto.ProductDto;
import com.mcommerce.product_service.entity.Product;
import com.mcommerce.product_service.exception.ResourceNotFoundException;
import com.mcommerce.product_service.mapper.ProductMapper;
import com.mcommerce.product_service.repository.ProductRepository;
import com.mcommerce.product_service.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;


    @Override
    public ProductDto createProduct(ProductDto dto, MultipartFile image) {

        Product product = productMapper.toEntity(dto);
        product.setActive(true);

        // Gestion de l’image
        if (image != null && !image.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Stocker le chemin de l’image
                product.setImageUrl("/uploads/" + fileName);

            } catch (IOException e) {
                throw new RuntimeException("Failed to store image", e);
            }
        }

        // Sauvegarde du produit
        Product savedProduct = productRepository.save(product);

        return productMapper.toDto(savedProduct);
    }


    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toDto(product);
    }


    @Override
    public ProductDto updateProduct(Long id, ProductDto dto, MultipartFile image) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id " + id)
                );

        if (dto.getName() != null) {
            product.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }

        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }

        if (image != null && !image.isEmpty()) {
            try {
                String fileName =
                        System.currentTimeMillis() + "_" + image.getOriginalFilename();

                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(
                        image.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING
                );

                product.setImageUrl("/uploads/" + fileName);

            } catch (IOException e) {
                throw new RuntimeException("Failed to store image", e);
            }
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }


    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        product.setActive(false);

        productRepository.save(product);
    }

    @Override
    public Page<ProductDto> getProducts(
            String keyword,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Product> products;

        if (keyword == null || keyword.isBlank()) {
            products = productRepository.findByActiveTrue(pageRequest);
        } else {
            products = productRepository.findByNameContainingIgnoreCaseAndActiveTrue(
                    keyword,
                    pageRequest
            );
        }

        return products.map(productMapper::toDto);
    }
}
