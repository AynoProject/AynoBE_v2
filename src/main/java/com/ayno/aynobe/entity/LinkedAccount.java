package com.ayno.aynobe.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(
        name = "linkedAccount",
        uniqueConstraints = @UniqueConstraint(
                name = "ux_linkedAccount_provider_providerId",
                columnNames = {"provider", "providerId"}
        ),
        indexes = {
                @Index(name = "ix_linkedAccount_userId", columnList = "userId")
        }
)
public class LinkedAccount extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkedAccountId;

    @Column(nullable = false, length = 20)
    private String provider;

    @Column(nullable = false, length = 100)
    private String providerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
