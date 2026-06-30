package com.mcommerce.payment_service.service.impl;

import com.mcommerce.payment_service.client.CartClient;
import com.mcommerce.payment_service.client.dto.CartResponse;
import com.mcommerce.payment_service.dto.CheckoutRequest;
import com.mcommerce.payment_service.dto.CheckoutResponse;
import com.mcommerce.payment_service.entity.PaymentTransaction;
import com.mcommerce.payment_service.enums.PaymentStatus;
import com.mcommerce.payment_service.exception.EmptyCartException;
import com.mcommerce.payment_service.repository.PaymentTransactionRepository;
import com.mcommerce.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final CartClient cartClient;
    private final PaymentTransactionRepository paymentRepository;

    @Override
    public CheckoutResponse checkout(Long userId, CheckoutRequest request) {

        CartResponse cart = cartClient.getMyCart();

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new EmptyCartException("Le panier est vide, paiement impossible");
        }

        PaymentTransaction transaction = PaymentTransaction.builder()
                .userId(userId)
                .cartId(cart.getCartId())
                .amount(cart.getTotalAmount())
                .status(PaymentStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .build();

        paymentRepository.save(transaction);

        boolean paymentSuccess = simulatePayment(cart);

        if (paymentSuccess) {
            transaction.setStatus(PaymentStatus.SUCCESS);

            cartClient.clearMyCart();
        } else {
            transaction.setStatus(PaymentStatus.FAILED);
        }

        paymentRepository.save(transaction);

        return CheckoutResponse.builder()
                .transactionId(transaction.getId())
                .userId(transaction.getUserId())
                .totalAmount(transaction.getAmount())
                .status(transaction.getStatus())
                .paymentMethod(transaction.getPaymentMethod())
                .createdDate(transaction.getCreatedDate())
                .build();
    }

    /**
     * Simulation simple du paiement
     */
    private boolean simulatePayment(CartResponse cart) {
        return cart.getTotalAmount().signum() > 0;
    }
}
