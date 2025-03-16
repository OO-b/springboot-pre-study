package com.study.hanghae.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//만든 필터는 Security Config에 등록해서 사용하도록 진행

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public LoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    //로그인 인증을 진행하기 위한 메소드 (override)
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
         // 요청을 가로채서 요청에 담긴 정보를 꺼냄
        // UsernamePasswordAuthenticationFilter가 유저정보를 UsernamePasswordAuthenticationToken에 담아서 Authentication Manager한테 전달 후 인증받음
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        System.out.println(username);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password, null); // principal, credential

        return authenticationManager.authenticate(authenticationToken); // DB에서 회원정보 받고 userDetailsService를 통해 검증을 진행함
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        System.out.println("success");

    }


    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        System.out.println("fail");


    }
}
