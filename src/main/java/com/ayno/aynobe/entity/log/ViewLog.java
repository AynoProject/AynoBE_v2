package com.ayno.aynobe.entity.log;

import com.ayno.aynobe.entity.Artifact;
import com.ayno.aynobe.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "view_log",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_viewlog_user_artifact_visitdate",
                        columnNames = {"artifactId", "userId", "visitDate"}
                )
        },
        indexes = {
                @Index(name = "idx_viewlog_artifact", columnList = "artifactId"),
                @Index(name = "idx_viewlog_user", columnList = "userId"),
                @Index(name = "idx_viewlog_visitDate", columnList = "visitDate")
        }
)
public class ViewLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewId; // PK

    // 결과물 FK
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artifactId", nullable = false)
    private Artifact artifact;

    // 사용자 FK
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    // 진입 시각
    @Column(name = "enteredAt", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime enteredAt;

    // 체류 시간(초)
    @Column(name = "dwellSec", nullable = false)
    private Long dwellSec;

    // “일 단위 중복 방지”를 위한 방문일자
    @Column(name = "visitDate", nullable = false, columnDefinition = "DATE")
    private LocalDate visitDate;

    // === 생성/업데이트 헬퍼 ===
    @PrePersist
    void prePersist() {
        if (enteredAt == null) enteredAt = LocalDateTime.now();
        if (visitDate == null) visitDate = enteredAt.toLocalDate();
        if (dwellSec == null) dwellSec = 0L;
    }

    /** 종료 시점(now 기준)으로 체류시간(초) 계산 */
    public void endNow() {
        this.dwellSec = Math.max(0,
                Duration.between(this.enteredAt, LocalDateTime.now()).getSeconds());
    }

    /** 명시적 종료 시각으로 체류시간(초) 계산 */
    public void endAt(LocalDateTime exitedAt) {
        this.dwellSec = Math.max(0,
                Duration.between(this.enteredAt, exitedAt).getSeconds());
    }
}
