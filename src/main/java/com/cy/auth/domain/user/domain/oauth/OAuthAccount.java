package com.cy.auth.domain.user.domain.oauth;

import com.cy.auth.domain.user.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "oauth_accounts", uniqueConstraints =
@UniqueConstraint(columnNames = {"provider, provider_user_id"}))
public class OAuthAccount {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuthProvider provider;

    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId;

    @Builder
    public OAuthAccount(User user, OAuthProvider provider, String providerUserId){
        this.user = user;
        this.provider = provider;
        this.providerUserId = providerUserId;
    }
}
