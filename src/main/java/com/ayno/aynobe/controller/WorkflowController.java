package com.ayno.aynobe.controller;

import com.ayno.aynobe.config.security.CustomUserDetails;
import com.ayno.aynobe.dto.common.PageResponseDTO;
import com.ayno.aynobe.dto.common.Response;
import com.ayno.aynobe.dto.workflow.*;
import com.ayno.aynobe.entity.User;
import com.ayno.aynobe.entity.enums.FlowType;
import com.ayno.aynobe.service.WorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Workflow", description = "워크플로우 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workflows")
@PreAuthorize("hasRole('USER')")
public class WorkflowController {

    private final WorkflowService workflowService;

    @Operation(
            summary = "워크플로우 카드 리스트"
    )
    @GetMapping
    public ResponseEntity<Response<PageResponseDTO<WorkflowCardDTO>>> getCardPage(
            @RequestParam(required = false) FlowType category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort // createdAt/likeCount/viewCount …
    ) {
        Sort sortSpec = Sort.by(sort.split(",")[0])
                .descending();
        if (sort.endsWith(",asc")) sortSpec = sortSpec.ascending();

        Pageable pageable = PageRequest.of(page, size, sortSpec);
        PageResponseDTO<WorkflowCardDTO> body = workflowService.getCardPage(category, pageable);
        return ResponseEntity.ok(Response.success(body));
    }


    @Operation(
            summary = "워크플로우 상세"
    )
    @GetMapping("/{workflowId}")
    public ResponseEntity<Response<WorkflowDetailResponseDTO>> getDetail(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long workflowId
    ) {
        User actor = (principal != null) ? principal.getUser() : null;
        WorkflowDetailResponseDTO body = workflowService.getDetail(actor, workflowId);
        return ResponseEntity.ok(Response.success(body));
    }


    @Operation(
            summary = "워크플로우 생성"
    )
    @PostMapping
    public ResponseEntity<Response<WorkflowCreateResponseDTO>> create(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody WorkflowCreateRequestDTO requestDTO
    ) {
        User owner = principal.getUser();
        WorkflowCreateResponseDTO res = workflowService.create(owner, requestDTO);
        return ResponseEntity.ok(Response.success(res));
    }


    @Operation(
            summary = "워크플로우 삭제"
    )
    @DeleteMapping("/{workflowId}")
    public ResponseEntity<Response<WorkflowDeleteResponseDTO>> delete(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long workflowId
    ) {
        User actor = principal.getUser();
        WorkflowDeleteResponseDTO res = workflowService.delete(actor, workflowId);
        return ResponseEntity.ok(Response.success(res));
    }
}
