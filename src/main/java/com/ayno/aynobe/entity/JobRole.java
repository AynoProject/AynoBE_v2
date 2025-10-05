package com.ayno.aynobe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "jobRole",
        uniqueConstraints = @UniqueConstraint(name = "ux_jobRole_label", columnNames = "jobRoleLabel")
)
@Entity
public class JobRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobRoleId;

    @Column(length = 50, nullable = false)
    private String jobRoleLabel;
}