package com.mcommerce.cart_service.service.impl;

import com.mcommerce.cart_service.dto.ProductResponse;
import com.mcommerce.cart_service.entity.Cart;
import com.mcommerce.cart_service.entity.CartItem;
import com.mcommerce.cart_service.feign.ProductClient;
import com.mcommerce.cart_service.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final ProductClient productClient;

    @Override
    public void addItem(Cart cart, Long productId) {

        if (cart.getItems() == null) {
            cart.setItems(new java.util.ArrayList<>());
        }

        CartItem existingItem = cart.getItems()
                .stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            return;
        }

        ProductResponse product = productClient.getProductById(productId);

        CartItem newItem = CartItem.builder()
                .cart(cart)
                .productId(product.getId())
                .productName(product.getName())
                .productImageUrl(product.getImageUrl())
                .price(product.getPrice())
                .quantity(1)
                .build();

        cart.getItems().add(newItem);
    }

    @Override
    public void updateQuantity(Cart cart, Long productId, int quantity) {

        if (cart.getItems() == null) {
            return;
        }

        cart.getItems()
                .stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    if (quantity <= 0) {
                        removeItem(cart, productId);
                    } else {
                        item.setQuantity(quantity);
                    }
                });
    }

    @Override
    public void removeItem(Cart cart, Long productId) {

        if (cart.getItems() == null) {
            return;
        }

        cart.getItems().removeIf(
                item -> item.getProductId().equals(productId)
        );
    }
}
