package com.mcommerce.cart_service.service.impl;

import com.mcommerce.cart_service.dto.CartResponse;
import com.mcommerce.cart_service.entity.Cart;
import com.mcommerce.cart_service.mapper.CartMapper;
import com.mcommerce.cart_service.repository.CartRepository;
import com.mcommerce.cart_service.service.CartItemService;
import com.mcommerce.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    private final CartMapper cartMapper;

    @Override
    public CartResponse getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(userId)
                            .totalAmount(BigDecimal.ZERO)
                            .build();
                    return cartRepository.save(newCart);
                });

        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse addProductToCart(Long userId, Long productId) {
        Cart cart = getOrCreateCart(userId);

        cartItemService.addItem(cart, productId);

        calculateTotalAmount(cart);
        cartRepository.save(cart);

        return cartMapper.toCartResponse(cart);
    }

    @Override
    public CartResponse removeProductFromCart(Long userId, Long productId) {
        Cart cart = getOrCreateCart(userId);

        cartItemService.removeItem(cart, productId);

        calculateTotalAmount(cart);
        cartRepository.save(cart);

        return cartMapper.toCartResponse(cart);
    }

    private Cart getCartEntityByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found for userId: " + userId)
                );
    }


    @Override
    public void clearCart(Long userId) {
        Cart cart = getCartEntityByUserId(userId);
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);
    }


    // ==========================
    // Méthodes internes
    // ==========================

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .userId(userId)
                                .totalAmount(BigDecimal.ZERO)
                                .build()
                ));
    }

    private void calculateTotalAmount(Cart cart) {
        BigDecimal total = cart.getItems()
                .stream()
                .map(item -> item.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(total);
    }

    @Override
    public CartResponse updateProductQuantity(Long userId, Long productId, int quantity) {

        Cart cart = getOrCreateCart(userId);

        cartItemService.updateQuantity(cart, productId, quantity);

        calculateTotalAmount(cart);
        cartRepository.save(cart);

        return cartMapper.toCartResponse(cart);
    }


}
