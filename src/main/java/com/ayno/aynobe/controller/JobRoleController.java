package com.ayno.aynobe.controller;

import com.ayno.aynobe.dto.common.Response;
import com.ayno.aynobe.dto.jobRole.JobRoleListItemResponseDTO;
import com.ayno.aynobe.service.JobRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "JobRole", description = "직무 관련 API")
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class JobRoleController {

    private final JobRoleService jobRoleService;

    @Operation(
            summary = "직무 전체 목록 조회",
            description = "온보딩/설정에서 선택용으로 사용"
    )
    @GetMapping
    public ResponseEntity<Response<List<JobRoleListItemResponseDTO>>> list() {
        return ResponseEntity.ok()
                .body(Response.success(jobRoleService.listAll()));
    }
}
