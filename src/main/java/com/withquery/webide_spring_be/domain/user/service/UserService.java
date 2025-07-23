package com.withquery.webide_spring_be.domain.user.service;

import com.withquery.webide_spring_be.domain.user.dto.UserRegistrationRequest;
import com.withquery.webide_spring_be.domain.user.dto.UserRegistrationResponse;

public interface UserService {

	UserRegistrationResponse registerUser(UserRegistrationRequest request);
} 