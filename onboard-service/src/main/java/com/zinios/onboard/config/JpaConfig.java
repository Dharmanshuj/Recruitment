package com.zinios.onboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.zinios.onboard.repository")
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfig {
    // JPA configuration for zinios-onboard-service
}