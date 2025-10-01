package com.ayno.aynobe.config.security.service;

import com.ayno.aynobe.config.security.CustomAdminDetails;
import com.ayno.aynobe.entity.Admin;
import com.ayno.aynobe.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomAdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String adminName) {
        Admin admin = adminRepository
                .findByAdminName(adminName)
                .orElseThrow(() -> new UsernameNotFoundException("해당 관리자를 찾을 수 없습니다"));

        return new CustomAdminDetails(admin);
    }
}
