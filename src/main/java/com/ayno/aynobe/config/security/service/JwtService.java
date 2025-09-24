package com.ayno.aynobe.config.security.service;

import com.ayno.aynobe.config.exception.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long ACCESS_EXP_MS  = 1000L * 60 * 15;         // 15분
    private static final long REFRESH_EXP_MS = 1000L * 60 * 60 * 24 * 7; // 7일

    // --- 발급 ---
    public String generateAccessToken(UserDetails user) {
        return generateToken(user.getUsername(), ACCESS_EXP_MS);
    }

    public String generateRefreshToken(UserDetails user) {
        return generateToken(user.getUsername(), REFRESH_EXP_MS);
    }

    private String generateToken(String subject, long expMillis) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expMillis))
                .signWith(signKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // --- 검증/파싱 ---
    public String extractUserId(String token) {
        try {
            return parseClaims(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            throw CustomException.unauthorized("JWT가 만료되었습니다.");
        } catch (MalformedJwtException | IllegalArgumentException e) {
            throw CustomException.unauthorized("잘못된 JWT 형식입니다.");
        } catch (SignatureException e) {
            throw CustomException.unauthorized("JWT 서명이 유효하지 않습니다.");
        } catch (JwtException e) {
            throw CustomException.unauthorized("JWT 검증에 실패했습니다.");
        }
    }

    // 액세스 토큰 검증: 사용자 일치 + 만료 여부
    public boolean isTokenValid(String token, UserDetails user) {
        try {
            Jws<Claims> jws = parseClaims(token);
            String sub = jws.getBody().getSubject();
            Date exp   = jws.getBody().getExpiration();
            return user.getUsername().equals(sub) && exp.after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    // 리프레시 토큰 검증(재발급 용): 만료만 확인
    public boolean isRefreshTokenValid(String token) {
        try {
            Date exp = parseClaims(token).getBody().getExpiration();
            return exp.after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signKey())
                .build()
                .parseClaimsJws(token);
    }

    private Key signKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}