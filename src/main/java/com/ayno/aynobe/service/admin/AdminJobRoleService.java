package com.ayno.aynobe.service.admin;

import com.ayno.aynobe.config.exception.CustomException;
import com.ayno.aynobe.dto.jobRole.JobRoleCreateRequestDTO;
import com.ayno.aynobe.dto.jobRole.JobRoleCreateResponseDTO;
import com.ayno.aynobe.dto.jobRole.JobRoleDeleteResponseDTO;
import com.ayno.aynobe.entity.JobRole;
import com.ayno.aynobe.repository.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminJobRoleService {
    private final JobRoleRepository jobRoleRepository;

    @Transactional
    public JobRoleCreateResponseDTO createJobRole(JobRoleCreateRequestDTO request) {
        String label = request.getJobRoleLabel() == null ? "" : request.getJobRoleLabel().trim();
        if (label.isEmpty()) {
            throw CustomException.badRequest("직무 라벨은 비어있을 수 없습니다.");
        }

        try {
            JobRole saved = jobRoleRepository.saveAndFlush(
                    JobRole.builder()
                            .jobRoleLabel(label)
                            .build()
            );
            return new JobRoleCreateResponseDTO(saved.getJobRoleId(), saved.getJobRoleLabel());

        } catch (DataIntegrityViolationException e) {
            throw CustomException.duplicate("이미 존재하는 직무입니다.");
        }
    }

    @Transactional
    public JobRoleDeleteResponseDTO delete(Integer jobRoleId) {
        try {
            jobRoleRepository.deleteById(jobRoleId);
            jobRoleRepository.flush();
            return new JobRoleDeleteResponseDTO(jobRoleId);
        } catch (EmptyResultDataAccessException e) {
            throw CustomException.notFound("해당 직무가 없습니다.");
        } catch (DataIntegrityViolationException e) {
            throw CustomException.conflict("사용 중인 직무는 삭제할 수 없습니다.");
        }
    }
}
