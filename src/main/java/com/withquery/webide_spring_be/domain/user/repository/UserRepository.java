package com.withquery.webide_spring_be.domain.user.repository;

import com.withquery.webide_spring_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByNickname(String nickname);

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);
} 