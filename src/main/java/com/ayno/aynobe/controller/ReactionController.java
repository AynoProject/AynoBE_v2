package com.ayno.aynobe.controller;

import com.ayno.aynobe.config.security.CustomUserDetails;
import com.ayno.aynobe.dto.common.Response;
import com.ayno.aynobe.dto.reaction.WorkflowLikeResponseDTO;
import com.ayno.aynobe.entity.User;
import com.ayno.aynobe.service.ReactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
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

    @Operation(summary = "워크플로우 좋아요 상태 조회(로그인 없어도 가능)")
    @GetMapping("/workflows/{workflowId}/like")
    public ResponseEntity<Response<WorkflowLikeResponseDTO>> getLike(
            @AuthenticationPrincipal @Nullable CustomUserDetails principal,
            @PathVariable Long workflowId
    ) {
        User actor = (principal != null) ? principal.getUser() : null;
        var res = reactionService.getWorkflowLike(actor, workflowId);
        return ResponseEntity.ok(Response.success(res));
    }

    @Operation(summary = "워크플로우 좋아요")
    @PostMapping("/workflows/{workflowId}/like")
    public ResponseEntity<Response<WorkflowLikeResponseDTO>> like(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long workflowId
    ) {
        User actor = principal.getUser();
        var res = reactionService.likeWorkflow(actor, workflowId);
        return ResponseEntity.ok(Response.success(res));
    }

    @Operation(summary = "워크플로우 좋아요 취소")
    @DeleteMapping("/workflows/{workflowId}/like")
    public ResponseEntity<Response<WorkflowLikeResponseDTO>> unlike(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long workflowId
    ) {
        User actor = principal.getUser();
        var res = reactionService.unlikeWorkflow(actor, workflowId);
        return ResponseEntity.ok(Response.success(res));
    }
}
