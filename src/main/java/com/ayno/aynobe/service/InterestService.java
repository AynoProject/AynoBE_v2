package com.ayno.aynobe.service;

import com.ayno.aynobe.dto.interest.InterestListItemResponseDTO;
import com.ayno.aynobe.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;

    @Transactional(readOnly = true)
    public List<InterestListItemResponseDTO> listAll() {
        return interestRepository.findAllDto();
    }
}
