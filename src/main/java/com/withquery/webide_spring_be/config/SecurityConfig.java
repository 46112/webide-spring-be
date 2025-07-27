package com.withquery.webide_spring_be.config;

import com.withquery.webide_spring_be.util.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Profile("dev")
public class SecurityConfig {

<<<<<<< HEAD
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(authz -> authz
				.anyRequest().permitAll()
			)
			.csrf(csrf -> csrf.disable())
			.headers(headers -> headers
				.frameOptions().sameOrigin()
			);

		return http.build();
	}
=======
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authz -> authz
                // 공개 엔드포인트
                .requestMatchers(PublicEndpoints.ENDPOINTS).permitAll()
                // 보호된 엔드포인트
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .headers(headers -> headers
                .frameOptions().sameOrigin()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
>>>>>>> main
} 