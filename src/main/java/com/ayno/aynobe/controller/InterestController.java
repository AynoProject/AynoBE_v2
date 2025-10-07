package com.ayno.aynobe.controller;

import com.ayno.aynobe.dto.common.Response;
import com.ayno.aynobe.dto.interest.InterestListItemResponseDTO;
import com.ayno.aynobe.service.InterestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Interest", description = "관심요소 관련 API")
@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class InterestController {
    private final InterestService interestService;

    @Operation(
            summary = "관심요소 전체 목록 조회",
            description = "온보딩/설정에서 선택용으로 사용"
    )
    @GetMapping
    public ResponseEntity<Response<List<InterestListItemResponseDTO>>> list() {
        return ResponseEntity.ok()
                .body(Response.success(interestService.listAll()));
    }
}
