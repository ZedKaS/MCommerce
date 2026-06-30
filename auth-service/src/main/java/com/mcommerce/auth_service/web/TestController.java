package com.mcommerce.auth_service.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/secured")
    public String securedEndpoint() {
        return "✅ Accès autorisé — JWT valide";
    }
}

