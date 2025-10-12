package com.ayno.aynobe.service;

import com.ayno.aynobe.config.exception.CustomException;
import com.ayno.aynobe.dto.reaction.WorkflowLikeResponseDTO;
import com.ayno.aynobe.entity.Reaction;
import com.ayno.aynobe.entity.User;
import com.ayno.aynobe.entity.Workflow;
import com.ayno.aynobe.entity.enums.ReactionType;
import com.ayno.aynobe.entity.enums.TargetType;
import com.ayno.aynobe.repository.ReactionRepository;
import com.ayno.aynobe.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final WorkflowRepository workflowRepository;

    @Transactional(readOnly = true)
    public WorkflowLikeResponseDTO getWorkflowLike(User actor, Long workflowId) {
        Workflow wf = workflowRepository.findById(workflowId)
                .orElseThrow(() -> CustomException.notFound("존재하지 않는 워크플로우입니다."));

        boolean liked = false;
        if (actor != null) {
            liked = reactionRepository.findByUser_UserIdAndTargetTypeAndTargetIdAndReactionType(
                    actor.getUserId(), TargetType.WORKFLOW, workflowId, ReactionType.LIKE
            ).isPresent();
        }

        return WorkflowLikeResponseDTO.builder()
                .workflowId(wf.getWorkflowId())
                .liked(liked)
                .likeCount(wf.getLikeCount())
                .build();
    }

    @Transactional
    public WorkflowLikeResponseDTO likeWorkflow(User actor, Long workflowId) {
        Workflow wf = workflowRepository.findById(workflowId)
                .orElseThrow(() -> CustomException.notFound("존재하지 않는 워크플로우입니다."));

        // 이미 눌렀으면 그대로 반환 (멱등)
        var existed = reactionRepository.findByUser_UserIdAndTargetTypeAndTargetIdAndReactionType(
                actor.getUserId(), TargetType.WORKFLOW, workflowId, ReactionType.LIKE);
        if (existed.isPresent()) {
            return WorkflowLikeResponseDTO.builder()
                    .workflowId(workflowId)
                    .liked(true)
                    .likeCount(wf.getLikeCount())
                    .build();
        }

        // 저장 + likeCount +1
        Reaction r = Reaction.builder()
                .user(actor)
                .targetId(workflowId)
                .targetType(TargetType.WORKFLOW)
                .reactionType(ReactionType.LIKE)
                .build();
        reactionRepository.save(r);
        workflowRepository.updateLikeCount(workflowId, 1);

        // 최신 카운트 반환 위해 다시 조회
        Workflow refreshed = workflowRepository.findById(workflowId)
                .orElseThrow(() -> CustomException.notFound("존재하지 않는 워크플로우입니다."));

        return WorkflowLikeResponseDTO.builder()
                .workflowId(workflowId)
                .liked(true)
                .likeCount(refreshed.getLikeCount())
                .build();
    }

    @Transactional
    public WorkflowLikeResponseDTO unlikeWorkflow(User actor, Long workflowId) {
        Workflow wf = workflowRepository.findById(workflowId)
                .orElseThrow(() -> CustomException.notFound("존재하지 않는 워크플로우입니다."));

        var existed = reactionRepository.findByUser_UserIdAndTargetTypeAndTargetIdAndReactionType(
                actor.getUserId(), TargetType.WORKFLOW, workflowId, ReactionType.LIKE);

        if (existed.isEmpty()) {
            // 멱등: 이미 취소 상태면 그대로 반환
            return WorkflowLikeResponseDTO.builder()
                    .workflowId(workflowId)
                    .liked(false)
                    .likeCount(wf.getLikeCount())
                    .build();
        }

        // 삭제 + likeCount -1 (하한 0은 UI에서만 고려, 서버는 단순 감소)
        reactionRepository.deleteByUserAndTargetAndType(actor.getUserId(), TargetType.WORKFLOW, workflowId, ReactionType.LIKE);
        workflowRepository.updateLikeCount(workflowId, -1);

        Workflow refreshed = workflowRepository.findById(workflowId)
                .orElseThrow(() -> CustomException.notFound("존재하지 않는 워크플로우입니다."));

        return WorkflowLikeResponseDTO.builder()
                .workflowId(workflowId)
                .liked(false)
                .likeCount(Math.max(0, refreshed.getLikeCount()))
                .build();
    }
}