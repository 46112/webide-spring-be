package com.withquery.webide_spring_be.domain.dbconnection.dto;

import lombok.Data;

@Data
public class DbConnectionRequest {
    private String name, url, username, password, driverClassName;
}