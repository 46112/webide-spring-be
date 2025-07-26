package com.withquery.webide_spring_be.domain.user.service;

import com.withquery.webide_spring_be.domain.user.dto.UserRegistrationRequest;
import com.withquery.webide_spring_be.domain.user.dto.UserRegistrationResponse;
import com.withquery.webide_spring_be.domain.user.dto.UserUpdateRequest;
import com.withquery.webide_spring_be.domain.user.dto.UserUpdateResponse;
import com.withquery.webide_spring_be.domain.user.dto.UserDeleteResponse;

public interface UserService {

    UserRegistrationResponse registerUser(UserRegistrationRequest request);
    
    UserUpdateResponse updateUser(String email, UserUpdateRequest request);
    
    UserDeleteResponse deleteUser(String email);
} 