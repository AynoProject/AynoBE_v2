package com.ayno.aynobe.service.admin;

import com.ayno.aynobe.config.exception.CustomException;
import com.ayno.aynobe.dto.interest.InterestCreateRequestDTO;
import com.ayno.aynobe.dto.interest.InterestCreateResponseDTO;
import com.ayno.aynobe.dto.interest.InterestDeleteResponseDTO;
import com.ayno.aynobe.entity.Interest;
import com.ayno.aynobe.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminInterestService {

    private final InterestRepository interestRepository;

    @Transactional
    public InterestCreateResponseDTO createInterest(InterestCreateRequestDTO request) {
        String label = request.getLabel() == null ? "" : request.getLabel().trim();
        if (label.isEmpty()) {
            throw CustomException.badRequest("관심요소 라벨은 비어있을 수 없습니다.");
        }

        try {
            Interest saved = interestRepository.saveAndFlush(
                    Interest.builder()
                            .interestLabel(label)
                            .build()
            );
            return new InterestCreateResponseDTO(saved.getInterestId(), saved.getInterestLabel());

        } catch (DataIntegrityViolationException e) {
            throw CustomException.duplicate("이미 존재하는 관심요소입니다.");
        }
    }

    @Transactional
    public InterestDeleteResponseDTO delete(Integer interestId) {
        try {
            interestRepository.deleteById(interestId);
            interestRepository.flush();
            return new InterestDeleteResponseDTO(interestId);
        } catch (EmptyResultDataAccessException e) {
            throw CustomException.notFound("해당 관심요소가 없습니다.");
        } catch (DataIntegrityViolationException e) {
            throw CustomException.conflict("사용 중인 관심요소는 삭제할 수 없습니다.");
        }
    }
}
