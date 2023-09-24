package com.ba.demo.core.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "demo-api.user.token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenConfig {

    private String jwtSecretKey;
    private int tokenTtlDays;
    private int webTokenTtlMinutes;
    private int activationTokenTtlDays;
    private int tokenCacheTtlSeconds;
    private long tokenCacheMaxSize;
    private int activationNumberTtlSeconds;
    private int activationNumberLength;

    public Duration tokenTtlDays() {
        return Duration.ofDays(tokenTtlDays);
    }

    public Duration webTokenTtlMinutes() {
        return Duration.ofMinutes(webTokenTtlMinutes);
    }

    public Duration tokenCacheTtlSeconds() {
        return Duration.ofSeconds(tokenCacheTtlSeconds);
    }
    public String jwtSecretKey() {
        return jwtSecretKey;
    }

}
