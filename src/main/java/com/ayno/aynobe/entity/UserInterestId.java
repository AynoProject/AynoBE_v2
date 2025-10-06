package com.ayno.aynobe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class UserInterestId implements Serializable {
    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "interestId", nullable = false)
    private Integer interestId;
}
