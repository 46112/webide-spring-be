package com.withquery.webide_spring_be.domain.dbconnection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
class ColumnInfo {
    private String name;
    private String type;
}