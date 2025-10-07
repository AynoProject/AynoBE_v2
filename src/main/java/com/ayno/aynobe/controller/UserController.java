package com.ayno.aynobe.controller;

import com.ayno.aynobe.config.security.CustomUserDetails;
import com.ayno.aynobe.dto.common.Response;
import com.ayno.aynobe.dto.user.OnboardingResponseDTO;
import com.ayno.aynobe.dto.user.OnboardingUpsertRequestDTO;
import com.ayno.aynobe.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "내 온보딩 정보 조회"
    )
    @GetMapping("/me/onboarding")
    public ResponseEntity<Response<OnboardingResponseDTO>> getMyOnboarding(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        return ResponseEntity.ok()
                .body(Response.success(userService.getMyOnboarding(principal.getUser().getUserId())));
    }

    @Operation(
            summary = "내 온보딩 정보 저장(Upsert/부분수정)",
            description = """
            - null 필드는 변경하지 않습니다.
            - interests: null=미변경, []=전체 해제, [ids]=교체(최대 3)
            - 멱등한 PUT. JPA 더티체킹으로 반영됩니다.
            """
    )
    @PutMapping("/me/onboarding")
    public ResponseEntity<Response<OnboardingResponseDTO>> upsertOnboarding(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody OnboardingUpsertRequestDTO request
    ) {
        return ResponseEntity.ok()
                .body(Response.success(userService.upsertOnboarding(principal.getUser().getUserId(), request)));
    }
}
