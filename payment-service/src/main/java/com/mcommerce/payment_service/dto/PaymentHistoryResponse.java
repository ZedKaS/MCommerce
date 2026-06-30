package com.mcommerce.payment_service.dto;

import com.mcommerce.payment_service.enums.PaymentMethod;
import com.mcommerce.payment_service.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentHistoryResponse {

    private Long transactionId;
    private Long userId;

    private BigDecimal amount;

    private PaymentStatus status;
    private PaymentMethod paymentMethod;

    private LocalDateTime createdDate;
}
