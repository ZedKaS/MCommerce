package com.mcommerce.cart_service.mapper;

import com.mcommerce.cart_service.dto.CartItemResponse;
import com.mcommerce.cart_service.dto.CartResponse;
import com.mcommerce.cart_service.entity.Cart;
import com.mcommerce.cart_service.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    public CartResponse toCartResponse(Cart cart) {
        CartResponse response = new CartResponse();

        response.setCartId(cart.getId());
        response.setUserId(cart.getUserId());
        response.setTotalAmount(cart.getTotalAmount());

        List<CartItemResponse> items = cart.getItems()
                .stream()
                .map(this::toCartItemResponse)
                .toList();

        response.setItems(items);
        int totalItems = cart.getItems()
                .stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        response.setTotalItems(totalItems);

        return response;
    }


    private CartItemResponse toCartItemResponse(CartItem item) {
        CartItemResponse response = new CartItemResponse();

        response.setProductId(item.getProductId());
        response.setProductName(item.getProductName());
        response.setProductImageUrl(item.getProductImageUrl());
        response.setPrice(item.getPrice());
        response.setQuantity(item.getQuantity());
        response.setTotalPrice(item.getTotalPrice());


        return response;
    }
}
