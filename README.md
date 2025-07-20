# WebIDE Spring Boot Backend

구름톤 풀스택 13회차 - 쿼리랑 팀의 WebIDE 만들기 백엔드 프로젝트입니다.

## 패키지 구조

```
src/main/java/com/withquery/webide_spring_be/
├── WebideSpringBeApplication.java    # 메인 애플리케이션 클래스
├── config/                           # 설정 클래스들
│   ├── SecurityConfig.java          # Spring Security 설정
│   └── OpenApiConfig.java           # OpenAPI/Swagger 설정
├── common/                           # 공통 컴포넌트
│   ├── controller/                   # 공통 컨트롤러
│   ├── service/                      # 공통 서비스
│   ├── repository/                   # 공통 리포지토리
│   ├── entity/                       # 공통 엔티티
│   ├── dto/                          # 공통 DTO
│   └── exception/                    # 공통 예외 처리
├── domain/                           # 도메인별 패키지
│   └── user/                         # 사용자 도메인
│       ├── controller/               # 사용자 컨트롤러
│       ├── service/                  # 사용자 서비스
│       ├── repository/               # 사용자 리포지토리
│       ├── entity/                   # 사용자 엔티티
│       └── dto/                      # 사용자 DTO
│       ...
└── util/                             # 유틸리티 클래스들
```

## 설정 클래스

### **OpenApiConfig**
OpenAPI/Swagger 문서화 설정을 담당합니다.
- API 메타데이터 설정
- GitBook 연동을 위한 OpenAPI JSON 생성

## 기술 스택

- **Framework**: Spring Boot 3.5.3
- **Language**: Java 17
- **Build Tool**: Gradle
- **Database**: H2 (개발), MySQL (운영)
- **ORM**: Spring Data JPA
- **Security**: Spring Security
- **Documentation**: SpringDoc OpenAPI
- **Container**: Docker

## API 엔드포인트

### **Health Check**
- `GET /health` - 애플리케이션 상태 확인

### **Swagger UI**
- `GET /swagger-ui/index.html` - API 문서 확인
- `GET /v3/api-docs` - OpenAPI JSON 다운로드
