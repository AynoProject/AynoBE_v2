package com.ayno.aynobe.service;

import com.ayno.aynobe.dto.artifact.ArtifactListItemResponseDTO;
import com.ayno.aynobe.dto.common.PageResponseDTO;
import com.ayno.aynobe.entity.Artifact;
import com.ayno.aynobe.entity.enums.FlowType;
import com.ayno.aynobe.entity.enums.VisibilityType;
import com.ayno.aynobe.repository.ArtifactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtifactService {

    private final ArtifactRepository artifactRepository;

    public PageResponseDTO<ArtifactListItemResponseDTO> listPublic(
            FlowType category, int page, int size, String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, resolveSort(sort));

        Page<Artifact> result = (category == null)
                ? artifactRepository.findByVisibility(VisibilityType.PUBLIC, pageable)
                : artifactRepository.findByVisibilityAndCategory(VisibilityType.PUBLIC, category, pageable);

        return PageResponseDTO.<ArtifactListItemResponseDTO>builder()
                .content(result.getContent().stream().map(ArtifactListItemResponseDTO::from).toList())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .hasNext(result.hasNext())
                .build();
    }

    private Sort resolveSort(String sort) {
        String field = "createdAt";
        Sort.Direction dir = Sort.Direction.DESC;

        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",", 2);

            if (parts.length >= 1 && !parts[0].isBlank()) {
                String candidate = parts[0].trim();
                if ("createdAt".equals(candidate) || "likeCount".equals(candidate) || "viewCount".equals(candidate)) {
                    field = candidate;
                }
            }
            if (parts.length == 2 && "asc".equalsIgnoreCase(parts[1].trim())) {
                dir = Sort.Direction.ASC;
            }
        }
        return Sort.by(dir, field);
    }
}
