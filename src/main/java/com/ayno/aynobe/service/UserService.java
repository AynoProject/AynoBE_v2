package com.ayno.aynobe.service;

import com.ayno.aynobe.config.exception.CustomException;
import com.ayno.aynobe.dto.user.OnboardingResponseDTO;
import com.ayno.aynobe.dto.user.OnboardingUpsertRequestDTO;
import com.ayno.aynobe.entity.Interest;
import com.ayno.aynobe.entity.JobRole;
import com.ayno.aynobe.entity.User;
import com.ayno.aynobe.repository.InterestRepository;
import com.ayno.aynobe.repository.JobRoleRepository;
import com.ayno.aynobe.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JobRoleRepository jobRoleRepository;
    private final InterestRepository interestRepository;

    @Transactional
    public OnboardingResponseDTO getMyOnboarding(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
        return OnboardingResponseDTO.from(user);
    }

    @Transactional
    public OnboardingResponseDTO upsertOnboarding(Long userId, OnboardingUpsertRequestDTO req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));

        // gender
        if (req.getGender() != null) {
            user.changeGender(req.getGender());
        }

        // aiUsageDepth
        if (req.getUsageDepth() != null) {
            user.changeAiUsageDepth(req.getUsageDepth());
        }

        // jobRole
        if (req.getJobRoleId() != null) {
            JobRole role = jobRoleRepository.findById(req.getJobRoleId())
                    .orElseThrow(() -> CustomException.notFound("유효하지 않은 직무(JobRole)입니다."));
            user.changeJobRole(role);
        }

        if (req.getInterestIds() != null) {
            if (req.getInterestIds().size() > 3) {
                throw CustomException.badRequest("관심분야는 최대 3개까지 선택할 수 있습니다.");
            }

            // 현재 ID (LAZY 컬렉션 1쿼리로 로드)
            Set<Integer> currentIds = user.getUserInterests().stream()
                    .map(ui -> ui.getInterest().getInterestId())
                    .collect(java.util.stream.Collectors.toSet());

            // 원하는 ID
            Set<Integer> desiredIds = new HashSet<>(req.getInterestIds());

            // 제거/추가 계산
            Set<Integer> idsToRemove = new HashSet<>(currentIds);
            idsToRemove.removeAll(desiredIds);      // 현재 - 원하는

            Set<Integer> idsToAdd = new HashSet<>(desiredIds);
            idsToAdd.removeAll(currentIds);         // 원하는 - 현재

            // 추가할 것만 DB 조회 (맵 불필요)
            List<Interest> interestsToAdd = Collections.emptyList();
            if (!idsToAdd.isEmpty()) {
                interestsToAdd = interestRepository.findAllById(idsToAdd);
                if (interestsToAdd.size() != idsToAdd.size()) {
                    throw CustomException.badRequest("유효하지 않은 관심요소 ID가 포함되어 있습니다.");
                }
            }

            // 도메인으로 적용 (orphanRemoval로 삭제 반영)
            user.updateInterests(idsToRemove, interestsToAdd);
        }

        return OnboardingResponseDTO.from(user);
    }
}
