package com.example.lostlink.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * ============================================================
 * OpenFeignConfig — Enables Declarative REST Clients
 * ============================================================
 * 
 * WHY THIS EXISTS:
 * In a microservices architecture, services need to communicate
 * with each other. OpenFeign lets you define an interface with
 * annotations, and Spring auto-generates the HTTP client at
 * runtime. Example:
 * 
 *   @FeignClient(name = "notification-service")
 *   interface NotificationClient {
 *       @GetMapping("/notifications/{userId}")
 *       List&lt;Notification&gt; getNotifications(@PathVariable Long userId);
 *   }
 * 
 * @EnableFeignClients — Scans for @FeignClient interfaces and
 *   creates proxy implementations at runtime.
 * ============================================================
 */
@Configuration
@EnableFeignClients(basePackages = "com.example.lostlink")
public class OpenFeignConfig {
}