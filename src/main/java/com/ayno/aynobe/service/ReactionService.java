package com.ayno.aynobe.service;

import com.ayno.aynobe.config.exception.CustomException;
import com.ayno.aynobe.dto.reaction.ArtifactLikeResponseDTO;
import com.ayno.aynobe.entity.Artifact;
import com.ayno.aynobe.entity.Reaction;
import com.ayno.aynobe.entity.User;
import com.ayno.aynobe.entity.enums.ReactionType;
import com.ayno.aynobe.entity.enums.TargetType;
import com.ayno.aynobe.repository.ArtifactRepository;
import com.ayno.aynobe.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final ArtifactRepository artifactRepository;

    @Transactional(readOnly = true)
    public ArtifactLikeResponseDTO getArtifactLike(User actor, Long artifactId) {
        Artifact artifact = artifactRepository.findById(artifactId)
                .orElseThrow(() -> CustomException.notFound("존재하지 않는 결과물입니다."));

        boolean liked = false;
        if (actor != null) {
            liked = reactionRepository.findByUser_UserIdAndTargetTypeAndTargetIdAndReactionType(
                    actor.getUserId(), TargetType.ARTIFACT, artifactId, ReactionType.LIKE
            ).isPresent();
        }

        return ArtifactLikeResponseDTO.builder()
                .artifactId(artifact.getArtifactId())
                .liked(liked)
                .likeCount(artifact.getLikeCount())
                .build();
    }

    @Transactional
    public ArtifactLikeResponseDTO likeArtifact(User actor, Long artifactId) {
        Artifact artifact = artifactRepository.findById(artifactId)
                .orElseThrow(() -> CustomException.notFound("존재하지 않는 결과물입니다."));

        var existed = reactionRepository.findByUser_UserIdAndTargetTypeAndTargetIdAndReactionType(
                actor.getUserId(), TargetType.ARTIFACT, artifactId, ReactionType.LIKE);

        if (existed.isPresent()) {
            return ArtifactLikeResponseDTO.builder()
                    .artifactId(artifactId)
                    .liked(true)
                    .likeCount(artifact.getLikeCount())
                    .build();
        }

        Reaction r = Reaction.builder()
                .user(actor)
                .targetId(artifactId)
                .targetType(TargetType.ARTIFACT)
                .reactionType(ReactionType.LIKE)
                .build();
        reactionRepository.save(r);
        artifactRepository.updateLikeCount(artifactId, 1);

        Artifact refreshed = artifactRepository.findById(artifactId)
                .orElseThrow(() -> CustomException.notFound("존재하지 않는 결과물입니다."));

        return ArtifactLikeResponseDTO.builder()
                .artifactId(artifactId)
                .liked(true)
                .likeCount(refreshed.getLikeCount())
                .build();
    }

    @Transactional
    public ArtifactLikeResponseDTO unlikeArtifact(User actor, Long artifactId) {
        Artifact artifact = artifactRepository.findById(artifactId)
                .orElseThrow(() -> CustomException.notFound("존재하지 않는 결과물입니다."));

        var existed = reactionRepository.findByUser_UserIdAndTargetTypeAndTargetIdAndReactionType(
                actor.getUserId(), TargetType.ARTIFACT, artifactId, ReactionType.LIKE);

        if (existed.isEmpty()) {
            return ArtifactLikeResponseDTO.builder()
                    .artifactId(artifactId)
                    .liked(false)
                    .likeCount(artifact.getLikeCount())
                    .build();
        }

        reactionRepository.deleteByUserAndTargetAndType(actor.getUserId(), TargetType.ARTIFACT, artifactId, ReactionType.LIKE);
        artifactRepository.updateLikeCount(artifactId, -1);

        Artifact refreshed = artifactRepository.findById(artifactId)
                .orElseThrow(() -> CustomException.notFound("존재하지 않는 결과물입니다."));

        return ArtifactLikeResponseDTO.builder()
                .artifactId(artifactId)
                .liked(false)
                .likeCount(Math.max(0, refreshed.getLikeCount()))
                .build();
    }
}