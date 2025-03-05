package com.study.hanghae.service;

import com.study.hanghae.entity.User;
import com.study.hanghae.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 사용자 회원가입
     * @param user {@link User}회원가입 사용자 정보
     * */
    public void signup(User user) {

        // 사용자 이름 중복확인
        if(isUserNameDup(user.getUsername())) {
            throw new RuntimeException("이미 존재하는 이름입니다.");
        }

        // 사용자 저장
        userRepository.save(user);
    }

    /**
     * 사용자 이름 중복여부 확인 
     * @param userName 사용자 이름
     * @return 중복여부
     * */
    private boolean isUserNameDup(String userName) {
        return userRepository.existsByUsername(userName); // SELECT COUNT(*) > 0 - true 존재한다. false 존재하지 않는다.

    }
}
