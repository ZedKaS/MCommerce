package com.mcommerce.payment_service.web;

import com.mcommerce.payment_service.dto.CheckoutRequest;
import com.mcommerce.payment_service.dto.CheckoutResponse;
import com.mcommerce.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CheckoutRequest request
    ) {
        CheckoutResponse response = paymentService.checkout(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

