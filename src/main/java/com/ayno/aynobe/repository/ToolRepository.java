package com.ayno.aynobe.repository;

import com.ayno.aynobe.entity.Tool;
import com.ayno.aynobe.entity.enums.ToolType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ToolRepository extends JpaRepository<Tool, Long> {
    Optional<Tool> findByToolName(String toolName);
    boolean existsByToolName(String toolName);

    Page<Tool> findByToolTypeAndToolNameContainingIgnoreCase(ToolType type, String q, Pageable pageable);
    Page<Tool> findByToolNameContainingIgnoreCase(String q, Pageable pageable);
    Page<Tool> findByToolType(ToolType type, Pageable pageable);

    List<Tool> findByToolIdIn(Collection<Long> toolIds);
}
