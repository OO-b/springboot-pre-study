package com.study.hanghae.authentication;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * JWT 관련된 클래스
 * 토큰 생성, 추출, 검증
 * */
@Slf4j
@Component
public class TokenProvider implements InitializingBean {
    private static final String AUTHORITIES_KEY = "auth";
    private final String secretKey;
    private final long tokenValidityInMilliseconds;
    private Key key;

    public TokenProvider(@Value("${spring.jwt.secret}") String secretKey,
                         @Value("${spring.jwt.token-validity-in-seconds}") long tokenValidityInMilliseconds) {
        this.secretKey = secretKey;
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds;
    }

    /**
     * InitializingBean 인터페이스 Method
     * 의존관계 주입이 끝난 후에 실행할 함수
     * */
    @Override
    public void afterPropertiesSet() throws Exception {
        // secret(Base64로 인코딩된) 값을 Base64로 디코딩해 key변수에 할당
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * Authentication 객체의 권한 정보로 토큰 생성
     * @param authentication
     * @return 토큰
     * */
    public String createToken(Authentication authentication) {

        // 권한값을 받아 하나의 문자열로 합침
        String authorities = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.joining(","));

        // 만료시간 설정
        long now = new Date().getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);
        String token = Jwts.builder()
                .setSubject(authentication.getName()) // 페이로드 주제 정보
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
        log.info("[createToken] 토큰생성완료 token :", token);

        return token;
    }

    /**
     * 토큰에서 인증 정보 조회 후 Authentication 객체 리턴
     * JWTFilter 에서 사용
     * @param token 토큰
     * @return
     * */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<? extends SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

//        User principal = new User(claims.getSubject(), "", authorities);
//
//        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
        return null;
    }

    /**
     * jwt 토큰을 검증하는 Method
     * JWTFilter 에서 사용
     * @param token 필터 정보
     * @return 토큰이 유효 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

}
