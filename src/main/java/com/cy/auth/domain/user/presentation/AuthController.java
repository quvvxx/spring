package com.cy.auth.domain.user.presentation;

import com.cy.auth.domain.user.application.AuthCookieService;
import com.cy.auth.domain.user.application.AuthService;
import com.cy.auth.domain.user.presentation.dto.request.LoginRequest;
import com.cy.auth.domain.user.presentation.dto.request.LogoutRequest;
import com.cy.auth.domain.user.presentation.dto.request.ReissueRequest;
import com.cy.auth.domain.user.presentation.dto.request.SignUpRequest;
import com.cy.auth.domain.user.presentation.dto.response.TokenResponse;
import com.cy.auth.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthCookieService authCookieService;
    private static final String REFRESH_TOKEN = "refresh_token";

    @PostMapping("/signup")
    public ApiResponse<Void> signUp(@Valid @RequestBody SignUpRequest request){
        authService.signUp(request);
        return ApiResponse.ok("회원가입이 성공적으로 완료되었습니다.");
    }

    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@Valid @RequestBody LoginRequest request){
        TokenResponse response = authService.login(request);
        return ApiResponse.ok(response, "로그인 되었습니다.");
    }

    @PostMapping("/reissue")
    public ApiResponse<TokenResponse> reissue(@Valid @RequestBody ReissueRequest request){
        TokenResponse response = authService.reissue(request);
        return ApiResponse.ok(response, "재발급 되었습니다.");
    }

    @PostMapping("/reissue/cookie")
    public ApiResponse<Void> reissueCookie(
            @CookieValue(name = REFRESH_TOKEN, required = false)
            String refreshToken, HttpServletResponse response){

        authCookieService.reissue(refreshToken, response);
        return ApiResponse.ok("재발급 되었습니다.");
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@Valid @RequestBody LogoutRequest request){
        authService.logout(request);
        return ApiResponse.ok("로그아웃 되었습니다.");
    }

    public ApiResponse<Void> logoutCookie(
            @CookieValue(name = REFRESH_TOKEN, required = false)
            String refreshToken, HttpServletResponse response){

        authCookieService.logout(refreshToken, response);
        return ApiResponse.ok("로그아웃 되었습니다.");
    }
}
