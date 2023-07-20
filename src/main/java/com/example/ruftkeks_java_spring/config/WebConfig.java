package com.example.ruftkeks_java_spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://52.78.61.142:443", "http://52.78.61.142:3000", "https://noticelog.co.kr", "https://www.noticelog.co.kr")
                .allowedMethods("GET", "POST", "PUT", "DELETE");

    }
}
