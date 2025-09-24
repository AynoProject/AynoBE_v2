package com.ayno.aynobe.config.security.filter;

import com.ayno.aynobe.config.exception.CustomException;
import com.ayno.aynobe.config.security.service.CustomUserDetailsService;
import com.ayno.aynobe.config.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String ACCESS_COOKIE  = "accessToken";
    private static final String REFRESH_COOKIE = "refreshToken";

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Value("${app.env:local}") private String appEnv;

    // 화이트리스트 (인증 제외)
    private static final List<String> WHITELIST_PREFIXES = List.of(
            "/swagger-ui",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/h2-console"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        final String uri = request.getRequestURI();
        if (isWhitelisted(uri)) {
            chain.doFilter(request, response);
            return;
        }

        // 이미 인증돼 있으면 패스
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }

        // accessToken 우선 시도
        String accessToken = getCookieValue(request, ACCESS_COOKIE);
        if (accessToken != null) {
            String username = jwtService.extractUserId(accessToken); // 내부에서 CustomException.* 던짐(401)
            UserDetails user = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(accessToken, user)) {
                setAuthentication(request, user);
                chain.doFilter(request, response);
                return;
            }
            throw CustomException.unauthorized("유효하지 않은 액세스 토큰입니다.");
        }

        // access 없거나 만료 → refresh 로 재발급 시도
        String refreshToken = getCookieValue(request, REFRESH_COOKIE);
        if (refreshToken != null && jwtService.isRefreshTokenValid(refreshToken)) {
            String username = jwtService.extractUserId(refreshToken); // 만료/시그니처시 401 던짐
            UserDetails user = userDetailsService.loadUserByUsername(username);

            String newAccess = jwtService.generateAccessToken(user);
            // Host-Only 쿠키(도메인 미설정), dev/prod에서 SameSite=None + Secure=true
            addAccessCookie(response, newAccess);

            setAuthentication(request, user);
            chain.doFilter(request, response);
            return;
        }

        // 둘 다 없거나 사용 불가
        throw CustomException.unauthorized("인증 토큰이 없습니다.");
    }

    /* ---------- helpers ---------- */

    private boolean isWhitelisted(String uri) {
        for (String prefix : WHITELIST_PREFIXES) {
            if (uri.startsWith(prefix)) return true;
        }
        return false;
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) {
            if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }

    private void setAuthentication(HttpServletRequest request, UserDetails user) {
        var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    // NOTE: 성공 핸들러와 동일 정책(Host-Only). local(http)에서 필요하면 SameSite=Lax/secure=false로 조정.
    private void addAccessCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from(ACCESS_COOKIE, token)
                .httpOnly(true)
                .secure(true)          // dev/prod https 전제
                .sameSite("None")      // 크로스사이트 전송을 위해 None
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}