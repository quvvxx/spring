package com.cy.auth.domain.user.application;

import com.cy.auth.domain.user.domain.oauth.OAuthAccount;
import com.cy.auth.domain.user.domain.oauth.OAuthAccountRepository;
import com.cy.auth.domain.user.domain.oauth.OAuthProvider;
import com.cy.auth.domain.user.domain.refreshtoken.RefreshToken;
import com.cy.auth.domain.user.domain.refreshtoken.RefreshTokenRepository;
import com.cy.auth.domain.user.domain.user.Role;
import com.cy.auth.domain.user.domain.user.User;
import com.cy.auth.domain.user.domain.user.UserRepository;
import com.cy.auth.domain.user.presentation.dto.request.oauth.OAuthRequest;
import com.cy.auth.domain.user.presentation.dto.response.TokenResponse;
import com.cy.auth.global.exception.BusinessException;
import com.cy.auth.global.exception.ErrorCode;
import com.cy.auth.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;
    private final OAuthAccountRepository oAuthRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RandomUsernameGenerator randomUsernameGenerator;
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenResponse login(OAuthRequest request){

        User user = findOrCreate(request);

        String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId(), user.getRole());

        refreshTokenRepository.save(RefreshToken.builder()
                .userId(user.getId()).token(refreshToken).build());

        return new TokenResponse(accessToken, refreshToken);

    }

    public User findOrCreate(OAuthRequest request){

        Optional<OAuthAccount> existing =
                oAuthRepository.findByProviderAndProviderUserId(
                        OAuthProvider.GOOGLE, request.providerUserId());

        if (existing.isPresent()) return existing.get().getUser();

        if (!request.emailVerified() ||
                request.email() == null || request.email().isBlank())
            throw new BusinessException(ErrorCode.OAUTH_EMAIL_NOT_FOUND);

        if (userRepository.existsByEmail(request.email()))
            throw new BusinessException(ErrorCode.USER_EMAIL_ALREADY_EXISTS);

        User user = User.builder()
                .email(request.email())
                .username(generateUniqueUsername())
                .password(null)
                .role(Role.USER)
                .build();

        userRepository.save(user);

        OAuthAccount oAuthAccount = OAuthAccount.builder()
                .user(user)
                .provider(OAuthProvider.GOOGLE)
                .providerUserId(request.providerUserId())
                .build();

        oAuthRepository.save(oAuthAccount);

        return user;
    }

    private String generateUniqueUsername(){
        for(int i=0; i<10; i++){
            String username = randomUsernameGenerator.generate();

            if (!userRepository.existsByUsername(username))
                return username;
        }
        throw new BusinessException(ErrorCode.USERNAME_GENERATION_FAILED);
    }
}
