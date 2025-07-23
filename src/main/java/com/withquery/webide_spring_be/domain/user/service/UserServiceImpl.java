package com.withquery.webide_spring_be.domain.user.service;

import com.withquery.webide_spring_be.domain.user.dto.UserRegistrationRequest;
import com.withquery.webide_spring_be.domain.user.dto.UserRegistrationResponse;
import com.withquery.webide_spring_be.domain.user.entity.User;
import com.withquery.webide_spring_be.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("이미 존재하는 이메일입니다.");
		}

		if (userRepository.existsByNickname(request.getNickname())) {
			throw new RuntimeException("이미 존재하는 닉네임입니다.");
		}

		User user = new User();
		user.setEmail(request.getEmail());
		user.setNickname(request.getNickname());
		user.setPassword(request.getPassword()); // TODO: 실제로는 암호화해야 함

		User savedUser = userRepository.save(user);
		return new UserRegistrationResponse("회원가입이 완료되었습니다.", savedUser.getId());
	}
} 