package com.mcommerce.payment_service.service;

import com.mcommerce.payment_service.dto.CheckoutRequest;
import com.mcommerce.payment_service.dto.CheckoutResponse;

public interface PaymentService {

    CheckoutResponse checkout(Long userId, CheckoutRequest request);
}
