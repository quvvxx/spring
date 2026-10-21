package com.cy.auth.global.config;

import com.cy.auth.global.security.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class OAuthConfig {

    private final OAuth2SuccessHandler successHandler;

    @Bean
    @Order(1)
    public SecurityFilterChain OAuthFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // CorsConfig를 @Bean으로 등록해서 Default써도 CorsConfig 적용
                .securityMatcher("/oauth2/**", "/login/oauth2/**")
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .oauth2Login(oauth -> oauth
                        .successHandler(successHandler));

        return http.build();

    }
}
