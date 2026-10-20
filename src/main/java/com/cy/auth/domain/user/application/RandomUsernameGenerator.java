package com.cy.auth.domain.user.application;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomUsernameGenerator {

    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private final SecureRandom random = new SecureRandom();

    public String generate(){
        StringBuilder username = new StringBuilder("g_");

        for(int i=0; i<8; i++){
            int index = random.nextInt(CHARS.length());
            username.append(CHARS.charAt(index));
        }

        return username.toString();
    }
}
