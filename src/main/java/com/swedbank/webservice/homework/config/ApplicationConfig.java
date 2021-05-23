package com.swedbank.webservice.homework.config;

import com.swedbank.webservice.homework.service.WebService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    
    @Bean
    public WebService webService() {
        return new WebService();
    }
}
