package com.ayno.aynobe.service;

import com.ayno.aynobe.config.exception.CustomException;
import com.ayno.aynobe.dto.common.PageResponseDTO;
import com.ayno.aynobe.dto.workflow.*;
import com.ayno.aynobe.entity.*;
import com.ayno.aynobe.entity.enums.FlowType;
import com.ayno.aynobe.entity.enums.TargetType;
import com.ayno.aynobe.entity.enums.VisibilityType;
import com.ayno.aynobe.repository.ReactionRepository;
import com.ayno.aynobe.repository.ToolRepository;
import com.ayno.aynobe.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final ReactionRepository reactionRepository;
    private final ToolRepository toolRepository;

    @Transactional(readOnly = true)
    public PageResponseDTO<WorkflowCardDTO> getCardPage(FlowType category, Pageable pageable) {
        Page<Workflow> page = (category == null)
                ? workflowRepository.findByVisibility(VisibilityType.PUBLIC, pageable)
                : workflowRepository.findByVisibilityAndCategory(VisibilityType.PUBLIC, category, pageable);

        List<WorkflowCardDTO> items = page.getContent().stream()
                .map(Workflow::toCardDTO)   // ← 도메인 메서드
                .toList();

        return PageResponseDTO.<WorkflowCardDTO>builder()
                .content(items)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .build();
    }

    @Transactional(readOnly = true)
    public WorkflowDetailResponseDTO getDetail(User actorOrNull, Long workflowId) {
        Workflow wf = workflowRepository.findWithAllByWorkflowId(workflowId)
                .orElseThrow(() -> CustomException.notFound("존재하지 않는 워크플로우입니다."));

        boolean isOwner = actorOrNull != null && wf.getUser().getUserId().equals(actorOrNull.getUserId());
        if (wf.getVisibility() != VisibilityType.PUBLIC && !isOwner) {
            throw CustomException.forbidden("열람 권한이 없습니다.");
        }

        return wf.toDetailDTO();      // ← 도메인 메서드
    }

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
                .sectionType(sectionDto.getSectionType())
                .promptRole(sectionDto.getPromptRole())
                .stepContent(sectionDto.getStepContent())
                .mediaUrl(sectionDto.getMediaUrl())
                .build();
    }

    @Transactional
    public WorkflowDeleteResponseDTO delete(User actor, Long workflowId) {
        // 1) 로드(+작성자)
        Workflow workflow = workflowRepository.findByWorkflowId(workflowId)
                .orElseThrow(() -> CustomException.notFound("존재하지 않는 워크플로우입니다."));

        // 2) 권한: 작성자만 허용
        boolean isOwner = workflow.getUser().getUserId().equals(actor.getUserId());
        if (!isOwner) {
            throw CustomException.forbidden("본인이 작성한 워크플로우만 삭제할 수 있습니다.");
        }

        // 3) Reaction 정리 (타깃: WORKFLOW)
        reactionRepository.deleteByTargetTypeAndTargetId(TargetType.WORKFLOW, workflowId);

        // 4) 루트 삭제 (cascade로 step/section 함께 제거)
        workflowRepository.delete(workflow);

        return WorkflowDeleteResponseDTO.builder()
                .workflowId(workflowId)
                .build();
    }
}
