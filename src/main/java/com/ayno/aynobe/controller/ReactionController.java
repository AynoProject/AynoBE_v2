package com.ayno.aynobe.controller;

import com.ayno.aynobe.config.security.CustomUserDetails;
import com.ayno.aynobe.dto.common.Response;
import com.ayno.aynobe.dto.reaction.ArtifactLikeResponseDTO;
import com.ayno.aynobe.service.ReactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Reaction", description = "리엑션 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reaction")
public class ReactionController {

    private final ReactionService reactionService;

    @Operation(summary = "결과물 좋아요 상태 조회", description = "로그인 없이도 조회 가능")
    @GetMapping("/artifacts/{artifactId}/like")
    public ResponseEntity<Response<ArtifactLikeResponseDTO>> getArtifactLike(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long artifactId
    ) {
        var actor = (principal != null) ? principal.getUser() : null;
        return ResponseEntity.ok(Response.success(
                reactionService.getArtifactLike(actor, artifactId)
        ));
    }

    @Operation(summary = "결과물 좋아요", description = "멱등 — 이미 좋아요면 그대로 반환")
    @PostMapping("/artifacts/{artifactId}/like")
    public ResponseEntity<Response<ArtifactLikeResponseDTO>> likeArtifact(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long artifactId
    ) {
        return ResponseEntity.ok(Response.success(
                reactionService.likeArtifact(principal.getUser(), artifactId)
        ));
    }

    @Operation(summary = "결과물 좋아요 취소", description = "멱등 — 이미 취소 상태면 그대로 반환")
    @DeleteMapping("/artifacts/{artifactId}/like")
    public ResponseEntity<Response<ArtifactLikeResponseDTO>> unlikeArtifact(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long artifactId
    ) {
        return ResponseEntity.ok(Response.success(
                reactionService.unlikeArtifact(principal.getUser(), artifactId)
        ));
    }
}