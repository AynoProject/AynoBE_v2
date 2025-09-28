package com.ayno.aynobe.service;

import com.ayno.aynobe.config.exception.CustomException;
import com.ayno.aynobe.config.security.CustomUserDetails;
import com.ayno.aynobe.config.security.service.JwtService;
import com.ayno.aynobe.dto.auth.LoginRequestDTO;
import com.ayno.aynobe.dto.auth.LoginTokensDTO;
import com.ayno.aynobe.dto.auth.SignUpRequestDTO;
import com.ayno.aynobe.dto.auth.SignUpResponseDTO;
import com.ayno.aynobe.entity.User;
import com.ayno.aynobe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Transactional
    public SignUpResponseDTO signUp(SignUpRequestDTO request) {
        User user = User.builder()
                .username(request.getUserId())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw CustomException.duplicate("이미 사용 중인 아이디입니다.");
        }

        return new SignUpResponseDTO("회원가입이 완료되었습니다.");
    }
}
