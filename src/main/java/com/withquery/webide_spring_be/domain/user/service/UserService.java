package com.withquery.webide_spring_be.domain.user.service;

import com.withquery.webide_spring_be.domain.user.dto.PasswordChangeRequest;
import com.withquery.webide_spring_be.domain.user.dto.PasswordChangeResponse;
import com.withquery.webide_spring_be.domain.user.dto.UserDeleteResponse;
import com.withquery.webide_spring_be.domain.user.dto.UserRegistrationRequest;
import com.withquery.webide_spring_be.domain.user.dto.UserRegistrationResponse;
import com.withquery.webide_spring_be.domain.user.dto.UserUpdateRequest;
import com.withquery.webide_spring_be.domain.user.dto.UserUpdateResponse;

public interface UserService {

	UserRegistrationResponse registerUser(UserRegistrationRequest request);

	UserUpdateResponse updateUser(String email, UserUpdateRequest request);

	UserDeleteResponse deleteUser(String email);

	PasswordChangeResponse changePassword(String email, PasswordChangeRequest request);
} 