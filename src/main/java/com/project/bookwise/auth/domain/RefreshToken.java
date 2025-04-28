package com.project.bookwise.auth.domain;

import java.time.LocalDateTime;

public class RefreshToken {

    private Long id;

    private String token;

    private LocalDateTime expiredAt;

    private LocalDateTime issuedAt;

    private Long userId;

    public RefreshToken(Long id, String token, LocalDateTime expiredAt, LocalDateTime issuedAt, Long userId) {
        this.id = id;
        this.token = token;
        this.expiredAt = expiredAt;
        this.issuedAt = issuedAt;
        this.userId = userId;
    }

    public RefreshToken() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
