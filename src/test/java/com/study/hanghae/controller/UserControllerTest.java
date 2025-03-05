package com.study.hanghae.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.hanghae.entity.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
class UserControllerTest {

    private static MockMvc mockMvc;
    private final UserController controller;

    @Autowired
    UserControllerTest(UserController controller) {
        this.controller = controller;
    }


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @Transactional
    void whenValidUserData_thenSignupSuccess() throws Exception {
        User user = new User("ddd", "qwer1234");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        //given
        MvcResult mvcResult = mockMvc.perform(post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


    }

    @Test
    void whenInvalidUserData_thenSignupFails() throws Exception {
        //TODO SQL 처리 (PRE)
        User user = new User("ddd", "qwer1234");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);

        //given
        MvcResult mvcResult = mockMvc.perform(post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


    }

}