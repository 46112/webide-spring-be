package com.withquery.webide_spring_be.domain.dbconnection.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DB 연결 생성/수정 요청")
public class DbConnectionRequest {
    @Schema(description = "커넥션 식별 이름", example = "MyProjectDB")
    private String name;

    @Schema(description = "JDBC URL", example = "jdbc:mysql://localhost:3306/ide_db")
    private String url;

    @Schema(description = "DB 사용자 이름", example = "dbuser")
    private String username;

    @Schema(description = "DB 비밀번호", example = "p@ssw0rd")
    private String password;

    @Schema(description = "JDBC 드라이버 클래스명", example = "com.mysql.cj.jdbc.Driver")
    private String driverClassName;

    @Schema(description = "프로젝트 ID", example = "42")
    private Long projectId;

    @Schema(description = "생성자(유저) ID", example = "7")
    private Long createdById;
}