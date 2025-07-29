package com.withquery.webide_spring_be.domain.dbconnection.dto;

import lombok.Data;

@Data
public class DbConnectionRequest {
    private String name, url, username, password, driverClassName;
    private Long projectId, createdById;   // 외래키로 받을 프로젝트 ID
}