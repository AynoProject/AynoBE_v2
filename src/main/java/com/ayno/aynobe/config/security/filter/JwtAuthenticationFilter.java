package com.ayno.aynobe.config.security.filter;

import com.ayno.aynobe.config.exception.CustomException;
import com.ayno.aynobe.config.security.oauth.CookieFactory;
import com.ayno.aynobe.config.security.service.CustomUserDetailsService;
import com.ayno.aynobe.config.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String ACCESS_COOKIE  = "accessToken";
    private static final String REFRESH_COOKIE = "refreshToken";

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final CookieFactory cookieFactory;

    // 화이트리스트 (인증 제외)
    private static final List<String> WHITELIST_PREFIXES = List.of(
            "/api/auth",
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
            try {
                String username = jwtService.extractUserId(accessToken);
                UserDetails user = userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(accessToken, user)) {
                    setAuthentication(request, user);
                    chain.doFilter(request, response);
                    return;
                }
            } catch (RuntimeException ignored) {}
        }

        // access 없거나 만료 → refresh 로 재발급 시도
        String refreshToken = getCookieValue(request, REFRESH_COOKIE);
        if (refreshToken != null && jwtService.isRefreshTokenValid(refreshToken)) {
            try {
                String username = jwtService.extractUserId(refreshToken);
                UserDetails user = userDetailsService.loadUserByUsername(username);
                String newAccess = jwtService.generateAccessToken(user);
                response.addHeader(HttpHeaders.SET_COOKIE, cookieFactory.access(newAccess).toString());
                setAuthentication(request, user);
            } catch (RuntimeException ignored) {}
        }
        chain.doFilter(request, response);
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
}