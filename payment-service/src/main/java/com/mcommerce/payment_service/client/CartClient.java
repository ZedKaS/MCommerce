package com.mcommerce.payment_service.client;

import com.mcommerce.payment_service.client.dto.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "CART-SERVICE")
public interface CartClient {

    /**
     * Récupérer MON panier (userId via header)
     */
    @GetMapping("/carts/me")
    CartResponse getMyCart();

    /**
     * Vider MON panier après paiement
     */
    @DeleteMapping("/carts/me")
    void clearMyCart();
}
