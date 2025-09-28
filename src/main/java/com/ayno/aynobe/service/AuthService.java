package com.ayno.aynobe.service;

import com.ayno.aynobe.config.security.CustomUserDetails;
import com.ayno.aynobe.config.security.service.JwtService;
import com.ayno.aynobe.dto.auth.LoginRequestDTO;
import com.ayno.aynobe.dto.auth.LoginTokensDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public LoginTokensDTO login(LoginRequestDTO request) {

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword())
        );
        var principal = (CustomUserDetails) auth.getPrincipal();

        String access  = jwtService.generateAccessToken(principal);
        String refresh = jwtService.generateRefreshToken(principal);

        return LoginTokensDTO.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .build();
    }
}
