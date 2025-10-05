package com.ayno.aynobe.entity;

import com.ayno.aynobe.entity.enums.AgeBand;
import com.ayno.aynobe.entity.enums.GenderType;
import com.ayno.aynobe.entity.enums.UsageDepthType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_users_username", columnNames = "username")
        }
)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 256)
    private String username;

    @Column(length = 512)
    private String passwordHash;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LinkedAccount> linkedAccounts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private GenderType gender;

    @Enumerated(EnumType.STRING)     // STRING 권장 (순서 변경 안전)
    @Column(length = 20)
    private AgeBand ageBand;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UsageDepthType aiUsageDepth;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserInterest> userInterests = new HashSet<>();
}
