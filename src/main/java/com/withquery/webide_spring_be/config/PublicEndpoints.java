package com.withquery.webide_spring_be.config;

public class PublicEndpoints {
    public static final String[] ENDPOINTS = {
        "/api/auth/**",
        "/api/users/register",
        "/health",
        "/h2-console/**",
        "/swagger-ui/**",
        "/v3/api-docs/**"
    };
} 