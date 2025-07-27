package com.withquery.webide_spring_be.domain.auth.service;

import com.withquery.webide_spring_be.domain.auth.dto.LoginRequest;
import com.withquery.webide_spring_be.domain.auth.dto.LoginResponse;
import com.withquery.webide_spring_be.domain.auth.dto.PasswordResetRequestRequest;
import com.withquery.webide_spring_be.domain.auth.dto.PasswordResetRequestResponse;
import com.withquery.webide_spring_be.domain.auth.dto.PasswordResetConfirmRequest;
import com.withquery.webide_spring_be.domain.auth.dto.PasswordResetConfirmResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    
    PasswordResetRequestResponse requestPasswordReset(PasswordResetRequestRequest request);
    
    PasswordResetConfirmResponse confirmPasswordReset(PasswordResetConfirmRequest request);
} 