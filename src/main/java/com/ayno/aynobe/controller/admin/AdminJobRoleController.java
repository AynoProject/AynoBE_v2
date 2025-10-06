package com.ayno.aynobe.controller.admin;

import com.ayno.aynobe.dto.common.Response;
import com.ayno.aynobe.dto.jobRole.JobRoleCreateRequestDTO;
import com.ayno.aynobe.dto.jobRole.JobRoleCreateResponseDTO;
import com.ayno.aynobe.dto.jobRole.JobRoleDeleteResponseDTO;
import com.ayno.aynobe.service.admin.AdminJobRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AdminJobRole", description = "관리자 직무 등록 및 삭제 관련 API")
@RestController
@RequestMapping("/api/admin/jobs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminJobRoleController {

    private final AdminJobRoleService adminJobRoleService;

    @Operation(
            summary = "직무 등록",
            description = "관리자가 직무를 등록합니다")
    @PostMapping
    public ResponseEntity<Response<JobRoleCreateResponseDTO>> create(
            @RequestBody JobRoleCreateRequestDTO reqest) {
        return ResponseEntity.ok()
                .body(Response.success(adminJobRoleService.createJobRole(reqest)));
    }

    @Operation(
            summary = "직무 삭제",
            description = "관리자가 등록되어있는 직무를 삭제합니다")
    @DeleteMapping("/{jobRoleId}")
    public ResponseEntity<Response<JobRoleDeleteResponseDTO>> delete(
            @PathVariable Integer jobRoleId) {
        return ResponseEntity.ok()
                .body(Response.success(adminJobRoleService.delete(jobRoleId)));
    }
}
