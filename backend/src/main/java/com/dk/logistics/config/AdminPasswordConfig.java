package com.dk.logistics.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "admin.password")
public class AdminPasswordConfig {

    private String adminL1;
    private String adminL2;
}
