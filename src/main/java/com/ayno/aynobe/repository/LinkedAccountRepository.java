package com.ayno.aynobe.repository;

import com.ayno.aynobe.entity.LinkedAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LinkedAccountRepository extends JpaRepository<LinkedAccount, Long> {
    @Query("""
    select la from LinkedAccount la
    join fetch la.user u
    where la.provider = :provider and la.providerId = :providerId
    """)
    Optional<LinkedAccount> findWithUser(String provider, String providerId);
}
