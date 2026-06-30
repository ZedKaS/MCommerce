package com.mcommerce.payment_service.repository;

import com.mcommerce.payment_service.entity.PaymentTransaction;
import com.mcommerce.payment_service.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentTransactionRepository
        extends JpaRepository<PaymentTransaction, Long> {

    /**
     * Historique des paiements d'un utilisateur (liste simple)
     */
    List<PaymentTransaction> findByUserId(Long userId);

    /**
     * Historique paginé des paiements d'un utilisateur
     */
    Page<PaymentTransaction> findByUserId(Long userId, Pageable pageable);

    /**
     * Paiements par statut (utile plus tard pour admin)
     */
    List<PaymentTransaction> findByStatus(PaymentStatus status);
}
