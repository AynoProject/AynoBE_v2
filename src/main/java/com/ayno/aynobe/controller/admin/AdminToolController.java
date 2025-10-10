package com.ayno.aynobe.controller.admin;

import com.ayno.aynobe.dto.common.Response;
import com.ayno.aynobe.dto.tool.ToolCreateRequestDTO;
import com.ayno.aynobe.dto.tool.ToolCreateResponseDTO;
import com.ayno.aynobe.dto.tool.ToolDeleteResponseDTO;
import com.ayno.aynobe.service.admin.AdminToolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AdminTool", description = "툴 관리(생성/삭제) 관리자 API")
@RestController
@RequestMapping("/api/admin/tools")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminToolController {

    private final AdminToolService adminToolService;

    @Operation(summary = "툴 생성", description = "툴 등록")
    @PostMapping
    public ResponseEntity<Response<ToolCreateResponseDTO>> create(
            @Valid @RequestBody ToolCreateRequestDTO req) {
        return ResponseEntity.ok()
                .body(Response.success(adminToolService.create(req)));
    }

    // delete
    @Operation(summary = "툴 삭제", description = "툴 PK로 삭제")
    @DeleteMapping("/{toolId}")
    public ResponseEntity<Response<ToolDeleteResponseDTO>> delete(
            @PathVariable Long toolId) {
        return ResponseEntity.ok()
                .body(Response.success(adminToolService.deleteById(toolId)));
    }
}
