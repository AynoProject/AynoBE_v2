package com.ayno.aynobe.config.security.oauth;

import com.ayno.aynobe.config.security.CustomUserDetails;
import com.ayno.aynobe.config.security.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Value("${app.env:local}") private String appEnv; // local | dev | prod

    @Value("${app.frontend.redirect.local}") private String redirectLocal;
    @Value("${app.frontend.redirect.dev}")   private String redirectDev;
    @Value("${app.frontend.redirect.prod}")  private String redirectProd;

    @Override
    public void onAuthenticationSuccess(jakarta.servlet.http.HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        final String accessToken  = jwtService.generateAccessToken(principal);
        final String refreshToken = jwtService.generateRefreshToken(principal);

        // 환경 정규화 + 리다이렉트 결정
        final String env = appEnv == null ? "local" : appEnv.trim().toLowerCase(Locale.ROOT);
        final String redirectTarget =
                "dev".equals(env)  ? redirectDev  :
                        "prod".equals(env) ? redirectProd : redirectLocal;

        // SameSite / Secure 결정
        final boolean secure  = !"local".equals(env);   // dev/prod는 https 전제
        final String  sameSite = secure ? "None" : "Lax"; // SameSite=None은 Secure 필수

        addCookie(response, "accessToken",  accessToken,  Duration.ofMinutes(15), secure, sameSite);
        addCookie(response, "refreshToken", refreshToken, Duration.ofDays(7),     secure, sameSite);

        log.info("[OAuth2Success] env={}, user={}, redirect={}", env, principal.getUsername(), redirectTarget);
        response.sendRedirect(redirectTarget);
    }

    private void addCookie(HttpServletResponse response,
                           String name, String value, Duration maxAge,
                           boolean secure, String sameSite) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path("/")
                .maxAge(maxAge)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}