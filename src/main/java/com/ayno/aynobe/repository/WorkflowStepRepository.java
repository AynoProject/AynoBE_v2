package com.ayno.aynobe.repository;

import com.ayno.aynobe.entity.WorkflowStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, Long> {
}
