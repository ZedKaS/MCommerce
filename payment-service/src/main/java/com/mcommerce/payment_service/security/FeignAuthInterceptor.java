package com.mcommerce.payment_service.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignAuthInterceptor {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes == null) return;

            HttpServletRequest request = attributes.getRequest();

            String userId = request.getHeader("X-User-Id");
            String role = request.getHeader("X-User-Role");
            String username = request.getHeader("X-Username");

            if (userId != null) {
                requestTemplate.header("X-User-Id", userId);
            }
            if (role != null) {
                requestTemplate.header("X-User-Role", role);
            }
            if (username != null) {
                requestTemplate.header("X-Username", username);
            }
        };
    }
}
