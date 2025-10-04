package com.ayno.aynobe.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(
        name = "userInterest",
        indexes = {
                @Index(name = "ix_user_interest_user", columnList = "userId"),
                @Index(name = "ix_user_interest_interest", columnList = "interestId")
        }
)
public class UserInterest {
    @EmbeddedId
    private UserInterestId userInterestId;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_userInterest_user"))
    private User user;

    @MapsId("interestId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "interest_id", foreignKey = @ForeignKey(name = "fk_userInterest_interest"))
    private Interest interest;

    public static UserInterest interestBuilder(User user, Interest interest) {
        return UserInterest.builder()
                .userInterestId(new UserInterestId(user.getUserId(), interest.getInterestId()))
                .user(user)
                .interest(interest)
                .build();
    }
}
