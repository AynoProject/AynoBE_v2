package com.ayno.aynobe.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(
        name = "interest",
        uniqueConstraints = @UniqueConstraint(name = "ux_interest_label", columnNames = "interestLabel")
)
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer interestId;

    @Column(length = 50, nullable = false)
    private String interestLabel;
}
