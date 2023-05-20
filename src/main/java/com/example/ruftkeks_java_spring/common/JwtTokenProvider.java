package com.example.ruftkeks_java_spring.common;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    private static final int accessTokenExpiredTime = 3600000;//60000;//3600000;
    private static final int refreshTokenExpiredTime = 86400000;//600000;//86400000;

    public JwtTokenProvider(@Value("${jwt.token.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtTokenInfo generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + accessTokenExpiredTime);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        Date refreshTokenExpiresIn = new Date(now + refreshTokenExpiredTime);
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return JwtTokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return TokenEnum.OK.caseBool;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            return TokenEnum.WRONG.caseBool;
        } catch (ExpiredJwtException e) {
            return TokenEnum.EXPIRED.caseBool;
        } catch (UnsupportedJwtException e) {
            return TokenEnum.NOT_SUPPORT.caseBool;
        } catch (IllegalArgumentException e) {
            return TokenEnum.ILLEGAL.caseBool;
        }
    }
    public String validateTokenDetail(String token) {
        try {
            Jws jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            String expString = Arrays.stream(jws.getBody().toString().substring(1, jws.getBody().toString().length()-1).split(", ")).filter(s -> s.startsWith("exp=")).findFirst().orElse(null);
            if (expString != null) {
                long expiredTime = Long.parseLong(expString.split("=")[1]);
                long currentTime = System.currentTimeMillis() / 1000;
                long remainMins = (expiredTime - currentTime);
                return TokenEnum.OK.text.concat(Long.toString(remainMins));
            }
            return TokenEnum.OK.text;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            return TokenEnum.WRONG.text;
        } catch (ExpiredJwtException e) {
            return TokenEnum.EXPIRED.text;
        } catch (UnsupportedJwtException e) {
            return TokenEnum.NOT_SUPPORT.text;
        } catch (IllegalArgumentException e) {
            return TokenEnum.ILLEGAL.text;
        }
    }
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
