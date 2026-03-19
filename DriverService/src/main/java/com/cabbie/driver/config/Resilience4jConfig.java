package com.cabbie.driver.config;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Resilience4jConfig {

    @Bean
    public Object retryEventLogger(RetryRegistry retryRegistry) {
        Retry retry = retryRegistry.retry("userService");
        retry.getEventPublisher()
                .onRetry(event -> System.out.println(
                        "Retry attempt: " + event.getNumberOfRetryAttempts()
                                + " | Exception: " + (event.getLastThrowable() != null ? event.getLastThrowable().getClass().getSimpleName() : "none")
                ));
        return new Object();
    }
}
