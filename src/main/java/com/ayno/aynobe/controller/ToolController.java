package com.ayno.aynobe.controller;

import com.ayno.aynobe.dto.common.Response;
import com.ayno.aynobe.dto.tool.ToolListItemResponseDTO;
import com.ayno.aynobe.entity.enums.ToolType;
import com.ayno.aynobe.service.ToolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Tool", description = "툴 공개 조회 API")
@RestController
@RequestMapping("/api/tools")
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
public class ToolController {

    private final ToolService toolService;

    @Operation(
            summary = "툴 목록",
            description = "툴 타입(toolTypeFilter)과 이름 검색(nameQuery)로 필터링 가능한 공개 리스트."
    )
    @GetMapping
    public ResponseEntity<Response<Page<ToolListItemResponseDTO>>> list(
            @Parameter(description = "툴 타입 필터", example = "AI")
            @RequestParam(required = false) ToolType toolTypeFilter,

            @Parameter(description = "툴 이름 검색어(부분일치)", example = "chat")
            @RequestParam(required = false) String nameQuery,

            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok()
                .body(Response.success(toolService.list(toolTypeFilter, nameQuery, pageable)));
    }
}
