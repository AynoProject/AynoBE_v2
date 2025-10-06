package com.ayno.aynobe.controller.admin;

import com.ayno.aynobe.dto.common.Response;
import com.ayno.aynobe.dto.interest.InterestCreateRequestDTO;
import com.ayno.aynobe.dto.interest.InterestCreateResponseDTO;
import com.ayno.aynobe.dto.interest.InterestDeleteResponseDTO;
import com.ayno.aynobe.service.admin.AdminInterestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AdminInterest", description = "관리자 관심요소 등록 및 삭제 관련 API")
@RestController
@RequestMapping("/api/admin/interests")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminInterestController {

    private final AdminInterestService adminInterestService;

    @Operation(
            summary = "관심요소 등록",
            description = "관리자가 관심요소를 등록합니다")
    @PostMapping
    public ResponseEntity<Response<InterestCreateResponseDTO>> createInterest(
            @RequestBody InterestCreateRequestDTO request) {
        return ResponseEntity.ok()
                .body(Response.success(adminInterestService.createInterest(request)));
    }

    @Operation(
            summary = "관심요소 삭제",
            description = "관리자가 등록되어있는 관심요소를 삭제합니다")
    @DeleteMapping("/{interestId}")
    public ResponseEntity<Response<InterestDeleteResponseDTO>> deleteInterest(
            @PathVariable Integer interestId)
    {
        return ResponseEntity.ok()
                .body(Response.success(adminInterestService.delete(interestId)));
    }
}
