package com.ayno.aynobe.service.admin;

import com.ayno.aynobe.config.exception.CustomException;
import com.ayno.aynobe.config.security.CustomAdminDetails;
import com.ayno.aynobe.config.security.service.JwtService;
import com.ayno.aynobe.dto.auth.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminAuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AdminAuthService(@Qualifier("adminAuthManager") AuthenticationManager authenticationManager,
                            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public LoginTokensDTO login(LoginRequestDTO request) {

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        CustomAdminDetails principal = (CustomAdminDetails) auth.getPrincipal();
        boolean isAdmin = principal.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (!isAdmin) {
            throw CustomException.forbidden("관리자 권한이 없습니다.");
        }

        String access  = jwtService.generateAccessToken(principal);
        String refresh = jwtService.generateRefreshToken(principal);

        return LoginTokensDTO.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .build();
    }
}
