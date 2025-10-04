package com.ayno.aynobe.repository;

import com.ayno.aynobe.entity.UserInterest;
import com.ayno.aynobe.entity.UserInterestId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInterestRepository extends JpaRepository<UserInterest, UserInterestId> {

}
