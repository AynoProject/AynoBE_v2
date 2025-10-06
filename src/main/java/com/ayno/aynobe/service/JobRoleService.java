package com.ayno.aynobe.service;

import com.ayno.aynobe.dto.jobRole.JobRoleListItemResponseDTO;
import com.ayno.aynobe.repository.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobRoleService {
    private final JobRoleRepository jobRoleRepository;

    @Transactional(readOnly = true)
    public List<JobRoleListItemResponseDTO> listAll() {
        return jobRoleRepository.findAllDto();
    }
}
