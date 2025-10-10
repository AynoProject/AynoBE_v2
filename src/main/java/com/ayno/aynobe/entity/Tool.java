package com.ayno.aynobe.entity;

import com.ayno.aynobe.entity.enums.ToolType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tool")
public class Tool extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long toolId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ToolType toolType;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String toolName;

    @NotBlank
    @Size(max = 512)
    @Column(nullable = false, length = 512)
    private String toolIconUrl;

    @NotBlank
    @Size(max = 512)
    @Column(nullable = false, length = 512)
    private String toolSiteUrl;
}