package com.cy.auth.global.security.oauth;

import com.cy.auth.domain.user.application.AuthCookieService;
import com.cy.auth.domain.user.application.OAuthService;
import com.cy.auth.domain.user.presentation.dto.request.oauth.OAuthRequest;
import com.cy.auth.domain.user.presentation.dto.response.TokenResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuthService oAuthService;
    private final AppProperties appProperties;
    private final AuthCookieService authCookieService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        OAuthRequest oAuthRequest = new OAuthRequest(
                oidcUser.getSubject(),
                oidcUser.getEmail(),
                Boolean.TRUE.equals(oidcUser.getEmailVerified()));

        TokenResponse tokenResponse = oAuthService.login(oAuthRequest);

        authCookieService.addCookies(response, tokenResponse);

        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();

        response.sendRedirect(appProperties.frontendUrl());
    }
}
