package com.ayno.aynobe.repository;

import com.ayno.aynobe.entity.Reaction;
import com.ayno.aynobe.entity.enums.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Reaction r " +
            "where r.targetType = :targetType and r.targetId = :targetId")
    void deleteByTargetTypeAndTargetId(TargetType targetType, Long targetId);
}
