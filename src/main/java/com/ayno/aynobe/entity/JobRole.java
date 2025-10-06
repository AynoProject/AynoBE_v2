package com.ayno.aynobe.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(
        name = "jobRole",
        uniqueConstraints = @UniqueConstraint(name = "ux_jobRole_label", columnNames = "jobRoleLabel")
)
public class JobRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobRoleId;

    @Column(length = 50, nullable = false)
    private String jobRoleLabel;
}