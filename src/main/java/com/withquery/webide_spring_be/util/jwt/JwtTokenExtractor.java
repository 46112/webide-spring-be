package com.withquery.webide_spring_be.util.jwt;

import org.springframework.util.StringUtils;

public class JwtTokenExtractor {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();

    /**
     * Authorization 헤더에서 JWT 토큰을 추출합니다.
     * 
     * @param authHeader Authorization 헤더 값
     * @return JWT 토큰 (Bearer 접두사 제거됨)
     * @throws IllegalArgumentException 유효하지 않은 Authorization 헤더인 경우
     */
    public static String extractToken(String authHeader) {
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
            throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
        }
        
        return authHeader.substring(BEARER_PREFIX_LENGTH);
    }

    /**
     * Authorization 헤더가 유효한지 확인합니다.
     * 
     * @param authHeader Authorization 헤더 값
     * @return 유효한 경우 true
     */
    public static boolean isValidAuthHeader(String authHeader) {
        return StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX);
    }
} 