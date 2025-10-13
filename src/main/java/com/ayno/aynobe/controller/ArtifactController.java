package com.ayno.aynobe.controller;

import com.ayno.aynobe.dto.artifact.ArtifactListItemResponseDTO;
import com.ayno.aynobe.dto.common.PageResponseDTO;
import com.ayno.aynobe.dto.common.Response;
import com.ayno.aynobe.entity.enums.FlowType;
import com.ayno.aynobe.service.ArtifactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Artifact", description = "결과물 조회 API")
@RestController
@RequestMapping("/api/artifacts")
@RequiredArgsConstructor
public class ArtifactController {

    private final ArtifactService artifactService;

    @Operation(
            summary = "공개 결과물 목록 조회",
            description = "visibility=PUBLIC 결과물을 최신순으로 페이지네이션하여 반환")
    @GetMapping
    public ResponseEntity<Response<PageResponseDTO<ArtifactListItemResponseDTO>>> list(
            @RequestParam(required = false) FlowType category,
            @RequestParam(defaultValue = "0") int page,   // 0-base
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort // createdAt|likeCount|viewCount
    ) {
        return ResponseEntity.ok(Response.success(
                artifactService.listPublic(category, page, size, sort)
        ));
    }
}