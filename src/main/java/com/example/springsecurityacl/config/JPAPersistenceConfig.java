package com.example.springsecurityacl.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.springsecurityacl.persistence.dao")
@PropertySource("classpath:application.properties")
@EntityScan(basePackages = {"com.example.springsecurityacl.persistence.entity"})
public class JPAPersistenceConfig {
}
