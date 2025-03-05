package com.study.hanghae.controller;

import com.study.hanghae.entity.User;
import com.study.hanghae.service.UserService;
import dto.SignupRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    //TODO ModelMapper 추가

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 사용자 회원가입
     * @param signupRequestDto {@link SignupRequestDto} 회원가입 사용자 정보
     * @return 결과메시지
     * */
    @PostMapping("/signup")
    public HashMap<String, String> signup(@RequestBody SignupRequestDto signupRequestDto) {
        //TODO 저장시 암호화
        //TODO 전역 예외처리
        String userName = signupRequestDto.getUsername(); //사용자 이름

        // 사용자 정보 저장
        User user = new User(userName, signupRequestDto.getPassword());
        userService.signup(user);

        //결과메시지 출력
        HashMap<String, String> result = new HashMap<>();
        result.put("message", String.format("Welcome %s", userName));
        return result;
    }

}
