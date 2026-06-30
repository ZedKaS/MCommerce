package com.mcommerce.product_service.dto;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    @NotBlank(message = "name is required")
    private String name;
    @Size(max = 1000)
    private String description;
    @NotNull(message = "price is required")
    @Positive(message = "price must be > 0")
    private BigDecimal price;

    private String imageUrl;

    private boolean active;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
