package com.study.hanghae.repository;

import com.study.hanghae.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * 사용자 이름 중복 여부 확인
     * @param username 사용자 이름
     * @return 중복여부
     * */
    boolean existsByUsername(String username);
}
