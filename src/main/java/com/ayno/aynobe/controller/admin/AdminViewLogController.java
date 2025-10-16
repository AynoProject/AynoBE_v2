package com.ayno.aynobe.controller.admin;

import com.ayno.aynobe.dto.common.PageResponseDTO;
import com.ayno.aynobe.dto.common.Response;
import com.ayno.aynobe.dto.log.ViewLogDetailResponseDTO;
import com.ayno.aynobe.service.admin.AdminViewLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "AdminViewLog", description = "관리자 전용: 뷰 로그 관리 API (MVP 최소)")
@RestController
@RequestMapping("/api/admin/viewlogs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminViewLogController {

    private final AdminViewLogService adminViewLogService;

    @Operation(summary = "특정 사용자 열람 내역 조회(페이지네이션)")
    @GetMapping("/users/{userId}")
    public ResponseEntity<Response<PageResponseDTO<ViewLogDetailResponseDTO>>> getUserViewLogs(
            @Parameter(description = "조회할 사용자명", example = "testuser")
            @RequestParam String username,
            @Parameter(description = "조회 시작일(YYYY-MM-DD, 포함)", example = "2025-10-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "조회 종료일(YYYY-MM-DD, 포함)", example = "2025-10-15")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @ParameterObject Pageable pageable
    ) {
        var payload = adminViewLogService.getUserViewLogsByUsername(username, from, to, pageable);
        return ResponseEntity.ok(Response.success(payload));
    }
}
