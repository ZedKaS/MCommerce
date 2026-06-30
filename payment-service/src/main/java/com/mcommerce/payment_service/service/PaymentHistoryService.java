package com.mcommerce.payment_service.service;

import com.mcommerce.payment_service.dto.PaymentHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentHistoryService {

    /**
     * Historique paginé des paiements d'un utilisateur
     */
    Page<PaymentHistoryResponse> getPaymentHistoryByUser(Long userId, Pageable pageable);

    /**
     * Historique paginé de tous les paiements (ADMIN)
     */
    Page<PaymentHistoryResponse> getAllPayments(Pageable pageable);
}
