package com.ecs.tutorial.oauth2.resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestController {

    @GetMapping("/authenticated")
    public String authenticatedndpoint(Principal principal) {
        return "authenticated endpoint with " + principal;
    }

    @GetMapping("/employee")
    public String employeeEndpoint(Principal principal) {
        return "employee endpoint with " + principal;
    }

    @GetMapping("/admin")
    public String adminEndpoint(Principal principal) {
        return "admin endpoint with " + principal;
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "public endpoint";
    }

}
