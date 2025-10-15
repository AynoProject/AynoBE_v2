package com.ayno.aynobe.service.admin;

import com.ayno.aynobe.config.exception.CustomException;
import com.ayno.aynobe.dto.common.PageResponseDTO;
import com.ayno.aynobe.dto.log.ViewLogDetailResponseDTO;
import com.ayno.aynobe.entity.User;
import com.ayno.aynobe.repository.UserRepository;
import com.ayno.aynobe.repository.ViewLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminViewLogService {

    private final ViewLogRepository viewLogRepository;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public PageResponseDTO<ViewLogDetailResponseDTO> getUserViewLogsByUsername(
            String username, LocalDate from, LocalDate to, Pageable pageable
    ) {
        if (username == null || username.isBlank()) {
            throw CustomException.forbidden("username은 필수입니다.");
        }

        Long userId = userRepository.findByUsername(username)
                .map(User::getUserId)
                .orElseThrow(() -> CustomException.notFound("존재하지 않는 username: " + username));

        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.plusDays(1).atStartOfDay().minusSeconds(1);

        var result = viewLogRepository
                .findByUser_UserIdAndEnteredAtBetweenOrderByEnteredAtDesc(userId, start, end, pageable);

        return PageResponseDTO.<ViewLogDetailResponseDTO>builder()
                .content(result.getContent().stream()
                        .map(v -> ViewLogDetailResponseDTO.builder()
                                .viewId(v.getViewId())
                                .artifactId(v.getArtifact().getArtifactId())
                                .userId(v.getUser().getUserId())
                                .enteredAt(v.getEnteredAt())
                                .dwellSec(v.getDwellSec())
                                .build())
                        .toList())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .hasNext(result.hasNext())
                .build();
    }
}
