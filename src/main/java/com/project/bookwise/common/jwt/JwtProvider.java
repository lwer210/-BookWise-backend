package com.project.bookwise.common.jwt;

import com.project.bookwise.common.custom.CustomUserDetails;
import com.project.bookwise.user.domain.User;
import com.project.bookwise.user.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final Key key;
    private final Long accessExpired;
    private final UserMapper userMapper;

    public JwtProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access.expired}") Long accessExpired,
            UserMapper userMapper
    ) {
        this.userMapper = userMapper;
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(bytes);
        this.accessExpired = accessExpired;
    }

    public String generateAccessToken(CustomUserDetails customUserDetails) {

        User user = userMapper.getUserInfoByUserId(customUserDetails.getUsername());

        if(user == null){
            throw new RuntimeException("User not found");
        }

        String authorities = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();

        return Jwts.builder()
                .setSubject("accessToken")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessExpired))
                .signWith(key, SignatureAlgorithm.HS256)
                .claim("auth", authorities)
                .claim("id", user.getId())
                .compact();
    }

    public Authentication getAuthentication(String token){
        Claims claims = parseToken(token);
        if(claims.get("auth") == null){
            throw new RuntimeException("권한 정보가 없습니다");
        }

        List<SimpleGrantedAuthority> auth = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        Long id = claims.get("id", Long.class);
        User user = userMapper.getUserInfoById(id);

        if(user == null){
            throw new RuntimeException("User not found");
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        return new UsernamePasswordAuthenticationToken(customUserDetails, null, auth);
    }

    // TODO 예외 처리 세분화 필요
    public Claims parseToken(String token) {
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // TODO 예외 처리 세분화 필요
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().build().parseClaimsJws(token).getBody();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
