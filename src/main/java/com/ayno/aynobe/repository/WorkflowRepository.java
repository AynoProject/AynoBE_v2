package com.ayno.aynobe.repository;

import com.ayno.aynobe.entity.Workflow;
import com.ayno.aynobe.entity.enums.FlowType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkflowRepository extends JpaRepository<Workflow, Long> {
    Optional<Workflow> findByWorkflowId(Long workflowId);

    // 카드 리스트용: 작성자(User)만 같이 로드해서 N+1 방지
    @EntityGraph(attributePaths = {"user"})
    Page<Workflow> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    Page<Workflow> findAllByCategory(FlowType category, Pageable pageable);

    // 디테일용: user, steps, stepSections, tool까지 한 방에 로드
    @EntityGraph(attributePaths = {
            "user",
            "workflowSteps",
            "workflowSteps.tool"
    })
    Optional<Workflow> findWithAllByWorkflowId(Long workflowId);
}
