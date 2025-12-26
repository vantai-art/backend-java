package com.ngovantai.example901.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // ✅ Cho phép credentials (cookies, authorization headers)
        config.setAllowCredentials(true);

        // ✅ Cho phép tất cả origins trong development (sau này sẽ giới hạn khi deploy)
        config.setAllowedOriginPatterns(Collections.singletonList("*"));

        // Hoặc chỉ định cụ thể:
        // config.setAllowedOrigins(Arrays.asList(
        // "http://localhost:3000",
        // "http://localhost:8081",
        // "https://your-production-domain.com"
        // ));

        // ✅ Cho phép tất cả headers
        config.setAllowedHeaders(Arrays.asList(
                "Origin",
                "Content-Type",
                "Accept",
                "Authorization",
                "X-Requested-With",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"));

        // ✅ Cho phép tất cả methods
        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS",
                "PATCH"));

        // ✅ Expose headers
        config.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type"));

        // ✅ Cache preflight request trong 1 giờ
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}