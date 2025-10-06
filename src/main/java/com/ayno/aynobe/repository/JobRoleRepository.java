package com.ayno.aynobe.repository;

import com.ayno.aynobe.dto.jobRole.JobRoleListItemResponseDTO;
import com.ayno.aynobe.entity.JobRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobRoleRepository  extends JpaRepository<JobRole, Integer> {
    @Query("""
        select new com.ayno.aynobe.dto.jobRole.JobRoleListItemResponseDTO(
            j.jobRoleId, j.jobRoleLabel
        )
        from JobRole j
        order by j.jobRoleLabel asc
    """)
    List<JobRoleListItemResponseDTO> findAllDto();
}
