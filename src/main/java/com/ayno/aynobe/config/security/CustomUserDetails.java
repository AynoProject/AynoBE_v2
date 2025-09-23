package com.ayno.aynobe.config.security;

import com.ayno.aynobe.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails, OAuth2User {
    private final User user;  // 우리가 만든 User 엔티티 감싸기

    // 권한 목록
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

    }

    // 일반 로그인 관련
    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 소셜 로그인 관련
    @Override
    public String getName() {
        return user.getUsername();
    }

    @Override
    public Object getAttribute(String name) {

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // 계정 만료 여부 (필요 시 DB 필드 추가해서 관리)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // 계정 잠금 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 비밀번호 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true;  // 계정 활성화 여부
    }
}