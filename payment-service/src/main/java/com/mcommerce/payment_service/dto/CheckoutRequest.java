package com.mcommerce.payment_service.dto;

import com.mcommerce.payment_service.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequest {

    @NotNull
    private PaymentMethod paymentMethod;
}
