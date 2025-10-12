package com.ayno.aynobe.repository;

import com.ayno.aynobe.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkflowRepository extends JpaRepository<Workflow, Long> {
    Optional<Workflow> findByWorkflowId(Long workflowId);
}
