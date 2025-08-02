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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "DB Connections", description = "DB 연결 정보 관리 API")
@RestController
@RequestMapping("/api/db-connections")
@RequiredArgsConstructor
public class DbConnectionController {
    private final DbConnectionService svc;

    @Operation(summary = "DB 연결 생성", description = "새로운 DB 연결 정보를 생성하고 커넥션 풀을 초기화합니다.")
    @PostMapping
    public ResponseEntity<DbConnectionResponse> create(
            @RequestBody DbConnectionRequest req) {
        DbConnectionResponse resp = svc.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @Operation(summary = "DB 연결 목록 조회", description = "저장된 모든 DB 연결 정보를 반환합니다.")
    @GetMapping
    public List<DbConnectionResponse> list() {
        return svc.list();
    }

    @Operation(summary = "DB 연결 상세 조회", description = "ID에 해당하는 DB 연결 정보를 반환합니다.")
    @GetMapping("/{id}")
    public DbConnectionResponse get(@PathVariable Long id) {
        return svc.get(id);
    }

    @Operation(summary = "DB 연결 수정", description = "ID에 해당하는 DB 연결 정보를 업데이트합니다.")
    @PutMapping("/{id}")
    public DbConnectionResponse update(
            @PathVariable Long id,
            @RequestBody DbConnectionRequest req) {
        return svc.update(id, req);
    }

    @Operation(summary = "DB 연결 삭제", description = "ID에 해당하는 DB 연결 정보를 삭제하고 커넥션 풀을 해제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "입력 정보 기반 DB 연결 테스트", description = "ID 없이 DB 연결 정보를 직접 입력하여 연결 테스트를 수행합니다.")
    @PostMapping("/test/direct")
    public Map<String, Object> testDirect(@RequestBody DbConnectionRequest req) {
        boolean success = svc.testDirectConnection(req);
        String message = success ? "DB 연결 성공" : "DB 연결 실패";
        return Map.of("success", success, "message", message);
    }

    @Operation(summary = "스키마 조회", description = "ID에 해당하는 DB 연결로부터 테이블/컬럼 스키마를 조회합니다.")
    @GetMapping("/{id}/schemas")
    public List<TableSchema> schemas(@PathVariable Long id) throws SQLException {
        return svc.getSchemas(id);
    }
}