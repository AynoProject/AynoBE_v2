package com.ayno.aynobe.service;

import com.ayno.aynobe.config.exception.CustomException;
import com.ayno.aynobe.dto.workflow.WorkflowCreateRequestDTO;
import com.ayno.aynobe.dto.workflow.WorkflowCreateResponseDTO;
import com.ayno.aynobe.entity.*;
import com.ayno.aynobe.repository.ToolRepository;
import com.ayno.aynobe.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final ToolRepository toolRepository;

    @Transactional
    public WorkflowCreateResponseDTO create(User owner, WorkflowCreateRequestDTO requestDto) {
        // 1) 요청값 유효성: stepNo / orderNo 중복 방지
        validateUniqueNumbers(requestDto);

        // 2) 사용되는 toolId 한 번에 로드 (N+1 예방)
        Map<Long, Tool> toolsById = preloadToolsById(requestDto);

        // 3) 루트(Workflow) 조립
        Workflow workflow = buildWorkflow(owner, requestDto);

        // 4) 스텝/섹션 조립 (양방향 세팅 + cascade 로 일괄 persist)
        requestDto.getSteps().forEach(stepDto -> {
            WorkflowStep step = buildWorkflowStep(workflow, stepDto, toolsById);
            workflow.getWorkflowSteps().add(step);

            stepDto.getSections().forEach(sectionDto -> {
                StepSection section = buildStepSection(step, sectionDto);
                // ⚠️ 엔티티가 stepSections 필드/게터를 사용하므로 다음과 같이 추가
                step.getStepSections().add(section);
            });
        });

        // 5) 저장
        Workflow saved = workflowRepository.save(workflow);
        return WorkflowCreateResponseDTO.builder()
                .workflowId(saved.getWorkflowId())
                .build();
    }

    // ===================== 내부 헬퍼 =====================

    /** stepNo / orderNo 중복 검사 (서비스 레벨 가드) */
    private void validateUniqueNumbers(WorkflowCreateRequestDTO requestDto) {
        Set<Integer> usedStepNos = new HashSet<>();

        requestDto.getSteps().forEach(stepDto -> {
            if (!usedStepNos.add(stepDto.getStepNo())) {
                throw CustomException.duplicate("중복된 stepNo 입니다: " + stepDto.getStepNo());
            }

            Set<Integer> usedOrderNosInStep = new HashSet<>();
            stepDto.getSections().forEach(sectionDto -> {
                if (!usedOrderNosInStep.add(sectionDto.getOrderNo())) {
                    throw CustomException.duplicate(
                            "스텝 " + stepDto.getStepNo() + " 내에서 중복된 orderNo 입니다: " + sectionDto.getOrderNo()
                    );
                }
            });
        });
    }

    /** 요청에 등장하는 toolId를 한 번에 로드하여 Map으로 반환 */
    private Map<Long, Tool> preloadToolsById(WorkflowCreateRequestDTO requestDto) {
        List<Long> toolIds = requestDto.getSteps().stream()
                .map(stepDto -> stepDto.getToolId())
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (toolIds.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Long, Tool> toolsById = toolRepository.findByToolIdIn(toolIds).stream()
                .collect(Collectors.toMap(Tool::getToolId, t -> t));

        // 요청에 포함된 toolId 중 존재하지 않는 것이 있으면 404
        for (Long toolId : toolIds) {
            if (!toolsById.containsKey(toolId)) {
                throw CustomException.notFound("존재하지 않는 Tool ID: " + toolId);
            }
        }
        return toolsById;
    }

    /** Workflow 루트 조립 */
    private Workflow buildWorkflow(User owner, WorkflowCreateRequestDTO requestDto) {
        return Workflow.builder()
                .category(requestDto.getCategory())
                .workflowTitle(requestDto.getWorkflowTitle())
                .user(owner)
                .visibility(requestDto.getVisibility())
                .thumbnailUrl(requestDto.getThumbnailUrl())
                .canvasJson(requestDto.getCanvasJson()) // ← 그대로 세팅
                .slug(requestDto.getSlug())
                .build();
    }

    /** WorkflowStep 조립 + Tool 참조 연결 */
    private WorkflowStep buildWorkflowStep(Workflow workflow,
                                           com.ayno.aynobe.dto.workflow.WorkflowCreateStepDTO stepDto,
                                           Map<Long, Tool> toolsById) {

        WorkflowStep step = WorkflowStep.builder()
                .workflow(workflow)
                .stepNo(stepDto.getStepNo())
                .stepTitle(stepDto.getStepTitle())
                .stepContent(stepDto.getStepContent())
                .build();

        if (stepDto.getToolId() != null) {
            // 이미 preload 단계에서 존재 확인했음
            step.setTool(toolsById.get(stepDto.getToolId()));
        }
        return step;
    }

    /** StepSection 조립 */
    private StepSection buildStepSection(WorkflowStep step,
                                         com.ayno.aynobe.dto.workflow.WorkflowCreateSectionDTO sectionDto) {
        return StepSection.builder()
                .workflowStep(step) // 부모 연결 (mappedBy = "workflowStep")
                .orderNo(sectionDto.getOrderNo())
                .sectionTitle(sectionDto.getSectionTitle())
                .stepType(sectionDto.getStepType())
                .promptRole(sectionDto.getPromptRole())
                .stepContent(sectionDto.getStepContent())
                .mediaUrl(sectionDto.getMediaUrl())
                .build();
    }
}
