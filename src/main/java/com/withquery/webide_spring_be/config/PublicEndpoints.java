package com.withquery.webide_spring_be.config;

public class PublicEndpoints {
    public static final String[] ENDPOINTS = {
        "/api/auth/**",
        "/api/users/register",
        "/api/projects/**",
        "/health",
        "/h2-console/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/v3/api-docs/**",
        "/ws/**",
    };
} 