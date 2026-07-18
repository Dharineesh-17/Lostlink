package com.example.lostlink.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * ============================================================
 * SwaggerConfig — OpenAPI / Swagger UI Configuration
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * Swagger (OpenAPI 3.0) auto-generates an interactive API
 * documentation page. Developers can:
 * - See all available endpoints with request/response schemas
 * - Try out APIs directly from the browser ("Try it out" button)
 * - See validation rules, required fields, and example values
 * 
 * Access at: http://localhost:8081/swagger-ui.html
 * 
 * @Configuration — Tells Spring this is a config class with @Bean definitions
 * ============================================================
 */
@Configuration
public class SwaggerConfig {

    @Value("${server.port:8081}")
    private int serverPort;

    /**
     * Configures the OpenAPI specification for this service.
     * 
     * @return OpenAPI object with API metadata and security scheme
     */
    @Bean
    public OpenAPI lostLinkOpenAPI() {
        return new OpenAPI()
                // API Metadata — appears on the Swagger UI header
                .info(new Info()
                        .title("LostLink API — Campus Lost & Found")
                        .description(
                                "A production-ready REST API for managing lost and found items on campus. " +
                                "Features include JWT authentication, role-based access control, " +
                                "item search, claim workflow, and dashboard analytics."
                        )
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("LostLink Team")
                                .email("lostlink@vsb.edu.in")
                        )
                )
                // Server URL
                .servers(List.of(
                        new Server().url("http://localhost:" + serverPort).description("Local Development")
                ))
                // JWT Security Scheme — adds an "Authorize" button in Swagger UI
                // Users can paste their JWT token here to test authenticated endpoints
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .bearerFormat("JWT")
                                        .scheme("bearer")
                                        .description("Enter your JWT token (without 'Bearer ' prefix)")
                        )
                );
    }
}