package com.example.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * ============================================================
 * Config Server Application
 * ============================================================
 *
 * WHY THIS EXISTS:
 * In production, you might have 10+ microservices, each with
 * database URLs, JWT secrets, and other configuration.
 * Managing these across multiple application.yml files is painful.
 *
 * Config Server provides centralized configuration:
 * - All configs stored in one place (Git repo, file system, or Vault)
 * - Services fetch their config at startup
 * - Changes can be pushed without redeploying services
 *
 * @EnableConfigServer — Turns this into a Spring Cloud Config Server
 * ============================================================
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}