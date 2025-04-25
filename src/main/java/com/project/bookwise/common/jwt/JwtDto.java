package com.project.bookwise.common.jwt;

public class JwtDto {

    private String accessToken;
    private String refreshToken;
    private String nickname;

    public JwtDto(String accessToken, String refreshToken, String nickname) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.nickname = nickname;
    }

    public JwtDto(builder builder){
        this.accessToken = builder.accessToken;
        this.refreshToken = builder.refreshToken;
        this.nickname = builder.nickname;
    }

    public JwtDto() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public static class builder{
        private String accessToken;
        private String refreshToken;
        private String nickname;

        public builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public JwtDto build() {
            return new JwtDto(this);
        }
    }
}
