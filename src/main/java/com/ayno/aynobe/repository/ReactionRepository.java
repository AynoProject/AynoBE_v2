package com.ayno.aynobe.repository;

import com.ayno.aynobe.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
}
