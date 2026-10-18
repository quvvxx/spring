package com.cy.auth.domain.user.presentation.dto.request.oauth;

public record OAuthRequest(
        String providerUserId,
        String email,
        boolean emailVerified
) {
}
