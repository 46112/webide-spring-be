package com.withquery.webide_spring_be.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("WebIDE Spring Boot API")
				.description("SQL 쿼리 시각화 IDE를 위한 Spring Boot 백엔드 API")
				.version("1.0.0")
				.contact(new Contact()
					.name("WithQuery Team")
					.email("contact@webide.com")
					.url("https://github.com/with-query/webide-spring-be"))
				.license(new License()
					.name("MIT License")
					.url("https://opensource.org/licenses/MIT")))
			.servers(List.of(
				new Server()
					.url("http://localhost:8080")
					.description("로컬 서버")
			))
			.components(new Components()
				.addSecuritySchemes("bearerAuth", new SecurityScheme()
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT")
					.description("JWT 토큰을 입력하세요. Bearer 접두사는 자동으로 추가됩니다.")))
			.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
	}
} 