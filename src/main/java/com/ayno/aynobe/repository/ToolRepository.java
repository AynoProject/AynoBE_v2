package com.ayno.aynobe.repository;

import com.ayno.aynobe.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ToolRepository extends JpaRepository<Tool, Long> {
    Optional<Tool> findByToolName(String toolName);
    boolean existsByToolName(String toolName);
}
