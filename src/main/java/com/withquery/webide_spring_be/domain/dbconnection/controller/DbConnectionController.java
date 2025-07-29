package com.withquery.webide_spring_be.domain.dbconnection.controller;

import com.withquery.webide_spring_be.domain.dbconnection.dto.DbConnectionRequest;
import com.withquery.webide_spring_be.domain.dbconnection.dto.DbConnectionResponse;
import com.withquery.webide_spring_be.domain.dbconnection.dto.TableSchema;
import com.withquery.webide_spring_be.domain.dbconnection.service.DbConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/db-connections")
@RequiredArgsConstructor
public class DbConnectionController {
    private final DbConnectionService svc;

    @PostMapping
    public ResponseEntity<DbConnectionResponse> create(@RequestBody DbConnectionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(svc.create(req));
    }

    @GetMapping
    public List<DbConnectionResponse> list() {
        return svc.list();
    }

    @GetMapping("/{id}")
    public DbConnectionResponse get(@PathVariable Long id) {
        return svc.get(id);
    }

    @PutMapping("/{id}")
    public DbConnectionResponse update(@PathVariable Long id,
                                       @RequestBody DbConnectionRequest req) {
        return svc.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/test")
    public Map<String, Boolean> test(@RequestBody Map<String, Long> body) {
        boolean ok = svc.testConnection(body.get("id"));
        return Collections.singletonMap("success", ok);
    }

    @GetMapping("/{id}/schemas")
    public List<TableSchema> schemas(@PathVariable Long id) throws SQLException {
        return svc.getSchemas(id);
    }
}
