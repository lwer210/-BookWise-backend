package com.project.bookwise.auth.mapper;

import com.project.bookwise.auth.domain.RefreshToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefreshTokenMapper {
    int insertRefreshToken(RefreshToken refreshToken);

    RefreshToken getRefreshTokenByToken(String token);
}
