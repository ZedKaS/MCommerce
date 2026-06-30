package com.mcommerce.payment_service.client.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {

    private Long productId;
    private String productName;
    private String productImageUrl;

    private BigDecimal price;
    private int quantity;
    private BigDecimal totalPrice;
}
