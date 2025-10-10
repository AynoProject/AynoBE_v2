package com.ayno.aynobe.service;

import com.ayno.aynobe.dto.tool.ToolListItemResponseDTO;
import com.ayno.aynobe.entity.Tool;
import com.ayno.aynobe.entity.enums.ToolType;
import com.ayno.aynobe.repository.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ToolService {

    private final ToolRepository toolRepository;

    @Transactional(readOnly = true)
    public Page<ToolListItemResponseDTO> list(
            ToolType toolTypeFilter,
            String nameQuery,
            Pageable pageable
    ) {
        // 입력 정규화
        final String normalizedNameQuery =
                (nameQuery == null) ? null : nameQuery.trim();

        final boolean hasTypeFilter = (toolTypeFilter != null);
        final boolean hasNameFilter =
                (normalizedNameQuery != null && !normalizedNameQuery.isBlank());

        final Page<Tool> page;

        if (hasTypeFilter && hasNameFilter) {
            page = toolRepository.findByToolTypeAndToolNameContainingIgnoreCase(
                    toolTypeFilter, normalizedNameQuery, pageable);
        } else if (hasTypeFilter) {
            page = toolRepository.findByToolType(toolTypeFilter, pageable);
        } else if (hasNameFilter) {
            page = toolRepository.findByToolNameContainingIgnoreCase(
                    normalizedNameQuery, pageable);
        } else {
            page = toolRepository.findAll(pageable);
        }

        return page.map(tool -> ToolListItemResponseDTO.from(tool));
    }
}
