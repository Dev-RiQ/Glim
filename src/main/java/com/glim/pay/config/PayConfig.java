package com.glim.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "portone")
@Data
public class PayConfig {
    private String apiKey;
    private String apiSecret;
    private String merchantCode;
}
