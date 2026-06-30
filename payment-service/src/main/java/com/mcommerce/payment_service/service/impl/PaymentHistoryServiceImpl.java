package com.mcommerce.payment_service.service.impl;

import com.mcommerce.payment_service.dto.PaymentHistoryResponse;
import com.mcommerce.payment_service.entity.PaymentTransaction;
import com.mcommerce.payment_service.repository.PaymentTransactionRepository;
import com.mcommerce.payment_service.service.PaymentHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentHistoryServiceImpl implements PaymentHistoryService {

    private final PaymentTransactionRepository paymentRepository;

    @Override
    public Page<PaymentHistoryResponse> getPaymentHistoryByUser(Long userId, Pageable pageable) {
        return paymentRepository.findByUserId(userId, pageable)
                .map(this::mapToDto);
    }

    @Override
    public Page<PaymentHistoryResponse> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    /**
     * Mapping Entity -> DTO
     */
    private PaymentHistoryResponse mapToDto(PaymentTransaction transaction) {
        return PaymentHistoryResponse.builder()
                .transactionId(transaction.getId())
                .userId(transaction.getUserId())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .paymentMethod(transaction.getPaymentMethod())
                .createdDate(transaction.getCreatedDate())
                .build();
    }
}
