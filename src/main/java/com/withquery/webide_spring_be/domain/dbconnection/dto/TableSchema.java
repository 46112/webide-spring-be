package com.withquery.webide_spring_be.domain.dbconnection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data @AllArgsConstructor
public class TableSchema {
    private String tableName;
    private List<ColumnInfo> columns = new ArrayList<>();
    public TableSchema(String tableName) {
        this.tableName = tableName;
    }

    public void addColumn(String name, String type) {
        columns.add(new ColumnInfo(name, type));
    }
}