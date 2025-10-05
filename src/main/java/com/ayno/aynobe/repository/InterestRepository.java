package com.ayno.aynobe.repository;

import com.ayno.aynobe.dto.interest.InterestListItemResponseDTO;
import com.ayno.aynobe.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Integer> {
    @Query("""
        select new com.ayno.aynobe.dto.interest.InterestListItemResponseDTO(
            i.interestId, i.interestLabel
        )
        from Interest i
        order by i.interestLabel asc
    """)
    List<InterestListItemResponseDTO> findAllDto();
}
