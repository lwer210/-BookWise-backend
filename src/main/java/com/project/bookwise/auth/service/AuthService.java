package com.project.bookwise.auth.service;

import com.project.bookwise.auth.domain.RefreshToken;
import com.project.bookwise.auth.mapper.RefreshTokenMapper;
import com.project.bookwise.common.custom.CustomUserDetails;
import com.project.bookwise.common.jwt.JwtDto;
import com.project.bookwise.common.jwt.JwtProvider;
import com.project.bookwise.common.jwt.RefreshTokenProvider;
import com.project.bookwise.user.domain.User;
import com.project.bookwise.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final RefreshTokenMapper refreshTokenMapper;

    public AuthService(
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtProvider jwtProvider,
            RefreshTokenProvider refreshTokenProvider, RefreshTokenMapper refreshTokenMapper
    ) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.refreshTokenMapper = refreshTokenMapper;
    }


    public User register(User user) {
        user.setRole("ROLE_ADMIN"); // TODO 추후 수정 예정
        if(userMapper.existsUserInfo(user.getUserId())){
            logger.debug("User Already Exists");
            throw new RuntimeException("User already exists");
        }

        String encodingPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodingPassword);

        int insertResult = userMapper.insertUser(user);

        if(insertResult == 1){
            return userMapper.getUserInfoById(user.getId());
        }

        throw new RuntimeException("회원가입 실패");
    }

    public JwtDto login(User loginRequest) {
        User user = userMapper.getUserInfoByUserId(loginRequest.getUserId());

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUserId(), loginRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        CustomUserDetails principal = (CustomUserDetails) authenticate.getPrincipal();

        String accessToken = jwtProvider.generateAccessToken(principal);
        RefreshToken refreshToken = refreshTokenProvider.generateRefreshToken(principal);

        refreshTokenMapper.insertRefreshToken(refreshToken);

        return new JwtDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .nickname(user.getNickname())
                .build();
    }

    public void refresh(JwtDto jwtDto) {
        String token = jwtDto.getRefreshToken();
        RefreshToken refreshToken = refreshTokenMapper.getRefreshTokenByToken(token);
        if(refreshToken == null){
            throw new RuntimeException("Refresh Token Not Found");
        }


    }
}
