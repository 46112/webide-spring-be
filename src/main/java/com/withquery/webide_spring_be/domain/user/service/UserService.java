package com.withquery.webide_spring_be.domain.user.service;

import com.withquery.webide_spring_be.domain.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> getAllUsers();
    
    Optional<UserDto> getUserById(Long id);
    
    Optional<UserDto> getUserByEmail(String email);
    
    UserDto createUser(UserDto userDto);
    
    UserDto updateUser(Long id, UserDto userDto);
    
    void deleteUser(Long id);
} 