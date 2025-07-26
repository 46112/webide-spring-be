package com.withquery.webide_spring_be.domain.auth.service;

import com.withquery.webide_spring_be.domain.auth.dto.LoginRequest;
import com.withquery.webide_spring_be.domain.auth.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
} 