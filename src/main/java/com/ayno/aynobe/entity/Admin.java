package com.ayno.aynobe.entity;

import com.ayno.aynobe.entity.enums.AdminRole;
import com.ayno.aynobe.entity.enums.AdminStatus;
import jakarta.persistence.*;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(
        name = "admins",
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_admins_username", columnNames = "adminName")
        },
        indexes = {
                @Index(name = "ix_admins_status", columnList = "status")
        }
)
public class Admin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @Column(length = 255, nullable = false)
    private String adminName;

    @Column(length = 255, nullable = false)
    private String passwordHash;

    /** 권한 등급 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdminRole role;

    /** 계정 상태 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdminStatus status;
}
