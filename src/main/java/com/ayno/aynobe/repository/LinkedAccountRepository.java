package com.ayno.aynobe.repository;

import com.ayno.aynobe.entity.LinkedAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkedAccountRepository extends JpaRepository<LinkedAccount, Long> {
    Optional<LinkedAccount> findByProviderAndProviderId(String provider, String providerId);
}
