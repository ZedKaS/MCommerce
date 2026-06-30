package com.mcommerce.payment_service.web;

import com.mcommerce.payment_service.dto.PaymentHistoryResponse;
import com.mcommerce.payment_service.service.PaymentHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentHistoryController {

    private final PaymentHistoryService paymentHistoryService;

    /**
     * Historique des paiements
     * - USER : ses paiements
     * - ADMIN : tous les paiements
     */
    @GetMapping("/history")
    public ResponseEntity<Page<PaymentHistoryResponse>> getPaymentHistory(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        PageRequest pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdDate").descending()
        );

        Page<PaymentHistoryResponse> history;

        if ("ADMIN".equals(role)) {
            // Admin → tous les paiements
            history = paymentHistoryService.getAllPayments(pageable);
        } else {
            // User → uniquement ses paiements
            history = paymentHistoryService.getPaymentHistoryByUser(userId, pageable);
        }

        return ResponseEntity.ok(history);
    }
}

