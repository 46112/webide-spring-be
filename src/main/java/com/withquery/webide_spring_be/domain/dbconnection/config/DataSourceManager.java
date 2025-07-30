package com.withquery.webide_spring_be.domain.dbconnection.config;

import com.withquery.webide_spring_be.domain.dbconnection.entity.DbConnection;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataSourceManager {

    private final Map<Long, HikariDataSource> dataSources = new ConcurrentHashMap<>();

    public void createPool(DbConnection cfg) {
        HikariConfig hc = new HikariConfig();
        hc.setJdbcUrl(cfg.getUrl());
        hc.setUsername(cfg.getUsername());
        hc.setPassword(cfg.getPassword());
        hc.setDriverClassName(cfg.getDriverClassName());
        hc.setMaximumPoolSize(10);
        hc.setMinimumIdle(2);
        HikariDataSource ds = new HikariDataSource(hc);
        dataSources.put(cfg.getId(), ds);
    }

    public void updatePool(DbConnection cfg) {
        removePool(cfg.getId());
        createPool(cfg);
    }

    public void removePool(Long id) {
        HikariDataSource ds = dataSources.remove(id);
        if (ds != null) ds.close();
    }

    public DataSource getDataSource(Long id) {
        return dataSources.get(id);
    }
}