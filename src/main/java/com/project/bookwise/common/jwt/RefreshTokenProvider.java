package com.project.bookwise.common.jwt;

import com.project.bookwise.auth.domain.RefreshToken;
import com.project.bookwise.common.custom.CustomUserDetails;
import com.project.bookwise.user.domain.User;
import com.project.bookwise.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Component
public class RefreshTokenProvider {

    private final Long refreshExpired;
    private final UserMapper userMapper;

    public RefreshTokenProvider(
            @Value("${jwt.refresh.expired}") Long refreshExpired,
            UserMapper userMapper
    ) {
        this.refreshExpired = refreshExpired;
        this.userMapper = userMapper;
    }

    public RefreshToken generateRefreshToken(CustomUserDetails customUserDetails) {
        User user = userMapper.getUserInfoByUserId(customUserDetails.getUsername());
        if(user == null) {
            throw new RuntimeException("User Not Found");
        }

        RefreshToken refreshToken = new RefreshToken();

        Date now = new Date();
        Date expired = new Date(now.getTime() + refreshExpired);
        
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setIssuedAt(now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        refreshToken.setExpiredAt(expired.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        refreshToken.setUserId(user.getId());

        return refreshToken;
    }
}
