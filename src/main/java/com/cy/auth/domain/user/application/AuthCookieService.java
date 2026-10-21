package com.cy.auth.domain.user.application;

import com.cy.auth.domain.user.presentation.dto.request.LogoutRequest;
import com.cy.auth.domain.user.presentation.dto.request.ReissueRequest;
import com.cy.auth.domain.user.presentation.dto.response.TokenResponse;
import com.cy.auth.global.exception.BusinessException;
import com.cy.auth.global.exception.ErrorCode;
import com.cy.auth.global.security.oauth.AppProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class AuthCookieService {

    private final AppProperties appProperties;
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private final AuthService authService;

    public void addCookies(HttpServletResponse response, TokenResponse tokenResponse){

        ResponseCookie accessCookie = ResponseCookie
                .from(ACCESS_TOKEN, tokenResponse.accessToken())
                .httpOnly(true)
                .secure(appProperties.cookieSecure())
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        ResponseCookie refreshCookie = ResponseCookie
                .from(REFRESH_TOKEN, tokenResponse.refreshToken())
                .httpOnly(true)
                .secure(appProperties.cookieSecure())
                .sameSite("Lax")
                .path("/api/auth")
                .maxAge(Duration.ofDays(14))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

    }

    public void reissue(String refreshToken, HttpServletResponse response){

        if (refreshToken == null || refreshToken.isBlank())
            throw new BusinessException(ErrorCode.INVALID_TOKEN);

        ReissueRequest request = new ReissueRequest(refreshToken);
        TokenResponse tokenResponse = authService.reissue(request);

        addCookies(response, tokenResponse);

    }

    public void deleteCookies(HttpServletResponse response){

        ResponseCookie accessCookie = ResponseCookie
                .from(REFRESH_TOKEN, "")
                .httpOnly(true)
                .secure(appProperties.cookieSecure())
                .sameSite("Lax")
                .path("/api")
                .maxAge(0)
                .build();

        ResponseCookie refreshCookie = ResponseCookie
                .from(REFRESH_TOKEN, "")
                .httpOnly(true)
                .secure(appProperties.cookieSecure())
                .sameSite("Lax")
                .path("/api/auth")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    public void logout(String refreshToken, HttpServletResponse response){

        LogoutRequest request = new LogoutRequest(refreshToken);
        if (refreshToken != null && !refreshToken.isBlank()) authService.logout(request);

        deleteCookies(response);
    }
}
