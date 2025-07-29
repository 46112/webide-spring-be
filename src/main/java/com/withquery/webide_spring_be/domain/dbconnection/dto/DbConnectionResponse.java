package com.withquery.webide_spring_be.domain.dbconnection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DbConnectionResponse {
    private Long id;
    private String name, url, username, driverClassName;//이름,경로,접속자명,드라이버클래스이름
    private Long projectId, createdById;   // 응답에 포함되는 프로젝트 ID,누가 만들었는지
}