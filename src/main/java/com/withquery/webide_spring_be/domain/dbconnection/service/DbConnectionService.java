package com.withquery.webide_spring_be.domain.dbconnection.service;

import com.withquery.webide_spring_be.domain.dbconnection.config.DataSourceManager;
import com.withquery.webide_spring_be.domain.dbconnection.dto.DbConnectionRequest;
import com.withquery.webide_spring_be.domain.dbconnection.dto.DbConnectionResponse;
import com.withquery.webide_spring_be.domain.dbconnection.dto.TableSchema;
import com.withquery.webide_spring_be.domain.dbconnection.entity.DbConnection;
import com.withquery.webide_spring_be.domain.dbconnection.repository.DbConnectionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DbConnectionService {
    private final DbConnectionRepository repo;
    private final DataSourceManager dsManager;

    @PostConstruct
    public void initPools() {
        repo.findAll().forEach(dsManager::createPool);
    }

    public DbConnectionResponse create(DbConnectionRequest req) {
        DbConnection saved = repo.save(DbConnection.builder()
                .name(req.getName())
                .url(req.getUrl())
                .username(req.getUsername())
                .password(req.getPassword())
                .driverClassName(req.getDriverClassName())
                .build()
        );
        dsManager.createPool(saved);
        return toResponse(saved);
    }

    public List<DbConnectionResponse> list() {
        return repo.findAll().stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public DbConnectionResponse get(Long id) {
        DbConnection cfg = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Not found"));
        return toResponse(cfg);
    }

    public DbConnectionResponse update(Long id, DbConnectionRequest req) {
        DbConnection cfg = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Not found"));
        cfg.setName(req.getName());
        cfg.setUrl(req.getUrl());
        cfg.setUsername(req.getUsername());
        cfg.setPassword(req.getPassword());
        cfg.setDriverClassName(req.getDriverClassName());
        DbConnection updated = repo.save(cfg);
        dsManager.updatePool(updated);
        return toResponse(updated);
    }

    public void delete(Long id) {
        repo.deleteById(id);
        dsManager.removePool(id);
    }

    public boolean testConnection(Long id) {
        DataSource ds = dsManager.getDataSource(id);
        try (Connection conn = ds.getConnection()) {
            return conn.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    public List<TableSchema> getSchemas(Long id) throws SQLException {
        DataSource ds = dsManager.getDataSource(id);
        try (Connection conn = ds.getConnection()) {
            DatabaseMetaData md = conn.getMetaData();
            ResultSet tables = md.getTables(null, null, "%", new String[]{"TABLE"});
            List<TableSchema> list = new ArrayList<>();
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                ResultSet cols = md.getColumns(null, null, tableName, "%");
                TableSchema ts = new TableSchema(tableName);
                while (cols.next()) {
                    ts.addColumn(cols.getString("COLUMN_NAME"), cols.getString("TYPE_NAME"));
                }
                list.add(ts);
            }
            return list;
        }
    }

    private DbConnectionResponse toResponse(DbConnection cfg) {
        return new DbConnectionResponse(
                cfg.getId(), cfg.getName(),
                cfg.getUrl(), cfg.getUsername(),
                cfg.getDriverClassName()
        );
    }
}
