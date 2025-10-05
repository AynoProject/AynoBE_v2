package com.ayno.aynobe.service;

import com.ayno.aynobe.dto.interest.InterestListItemResponseDTO;
import com.ayno.aynobe.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;

    public List<InterestListItemResponseDTO> listAll() {
        return interestRepository.findAllDto();
    }
}
