package com.withquery.webide_spring_be.domain.dbconnection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DbConnectionResponse {
    private Long id;
    private String name, url, username, driverClassName;
}