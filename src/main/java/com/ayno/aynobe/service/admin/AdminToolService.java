package com.ayno.aynobe.service.admin;

import com.ayno.aynobe.config.exception.CustomException;
import com.ayno.aynobe.dto.tool.ToolCreateRequestDTO;
import com.ayno.aynobe.dto.tool.ToolCreateResponseDTO;
import com.ayno.aynobe.dto.tool.ToolDeleteResponseDTO;
import com.ayno.aynobe.entity.Tool;
import com.ayno.aynobe.repository.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminToolService {

    private final ToolRepository adminToolRepository;

    @Transactional
    public ToolCreateResponseDTO create(ToolCreateRequestDTO dto) {
        if (adminToolRepository.existsByToolName(dto.getToolName())) {
            throw CustomException.duplicate(dto.getToolName() + " 이 이미 존재합니다");
        }

        Tool tool = Tool.builder()
                .toolType(dto.getToolType())
                .toolName(dto.getToolName())
                .toolIconUrl(dto.getToolIconUrl())
                .toolSiteUrl(dto.getToolSiteUrl())
                .build();

        try {
            adminToolRepository.saveAndFlush(tool);
            return ToolCreateResponseDTO.from(tool);
        } catch (DataIntegrityViolationException e) {
            throw CustomException.duplicate(dto.getToolName() + " 이 이미 존재합니다");
        }
    }

    @Transactional
    public ToolDeleteResponseDTO deleteById(Long toolId) {
        if (!adminToolRepository.existsById(toolId)) {
            throw CustomException.notFound("해당 도구가 없습니다.");
        }

        try {
            adminToolRepository.deleteById(toolId);
            adminToolRepository.flush();
            return new ToolDeleteResponseDTO(toolId);
        } catch (EmptyResultDataAccessException e) {
            throw CustomException.notFound("해당 도구가 없습니다.");
        } catch (DataIntegrityViolationException e) {
            throw CustomException.conflict("사용 중인 도구는 삭제할 수 없습니다.");
        }
    }
}
