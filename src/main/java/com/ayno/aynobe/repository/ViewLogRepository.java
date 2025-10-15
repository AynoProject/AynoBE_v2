package com.ayno.aynobe.repository;

import com.ayno.aynobe.entity.log.ViewLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ViewLogRepository extends JpaRepository<ViewLog, Long> {

    Optional<ViewLog> findByArtifact_ArtifactIdAndUser_UserIdAndVisitDate(
            Long artifactId, Long userId, LocalDate visitDate
    );

    Page<ViewLog> findByUser_UserIdAndEnteredAtBetweenOrderByEnteredAtDesc(
            Long userId, LocalDateTime from, LocalDateTime to, Pageable pageable
    );
}