package com.ayno.aynobe.entity;

import com.ayno.aynobe.entity.enums.AgeBand;
import com.ayno.aynobe.entity.enums.GenderType;
import com.ayno.aynobe.entity.enums.UsageDepthType;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

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

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AgeBand ageBand;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UsageDepthType aiUsageDepth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobRoleId")
    private JobRole jobRole;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserInterest> userInterests = new HashSet<>();

    public void changeGender(GenderType gender) { this.gender = gender; }

    public void changeAiUsageDepth(UsageDepthType depth) { this.aiUsageDepth = depth; }

    public void changeJobRole(JobRole jobRole) { this.jobRole = jobRole; }

    public void updateInterests(Set<Integer> interestIdsToRemove,
                                Collection<Interest> interestsToAdd) {
        if (!interestIdsToRemove.isEmpty()) {
            this.userInterests.removeIf(ui ->
                    interestIdsToRemove.contains(ui.getInterest().getInterestId()));
        }
        if (!interestsToAdd.isEmpty()) {
            for (Interest interest : interestsToAdd) {
                this.userInterests.add(UserInterest.interestBuilder(this, interest));
            }
        }
    }
}
