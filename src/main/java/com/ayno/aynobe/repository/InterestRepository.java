package com.ayno.aynobe.repository;

import com.ayno.aynobe.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Integer> {
    boolean existsByInterestLabel(String interestLabel);
}
