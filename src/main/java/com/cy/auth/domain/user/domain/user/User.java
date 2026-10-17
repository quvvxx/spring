package com.cy.auth.domain.user.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 10, unique = true)
    private String username;

    @Column
    private String password;

    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @Builder
    private User(String email, String username, String password, Role role){
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static User createGoogle(String email, String username, Role role){
        return User.builder()
                .email(email)
                .username(username)
                .role(role)
                .build();
    }

    public void changeUsername(String username){
        this.username = username;
    }

}
