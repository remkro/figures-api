package com.geofigeo.figuresapi.controller;

import com.geofigeo.figuresapi.BaseIntegrationTest;
import com.geofigeo.figuresapi.repository.UserRepository;
import com.geofigeo.figuresapi.utils.TestUtils;
import com.geofigeo.figuresapi.dto.JwtRequestDto;
import com.geofigeo.figuresapi.dto.SignUpRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JwtAuthenticationControllerIT extends BaseIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    void should_get_jwt_token() throws Exception {
        // Add user
        SignUpRequestDto signUpRequest = TestUtils.createSignUpRequest();
        String signUpRequestJson = mapper.writeValueAsString(signUpRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestJson))
                .andDo(print());

        // Get JWT token with created user credentials
        JwtRequestDto request = new JwtRequestDto("jsmith", "password123");
        String jwtRequest = mapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authenticate")
                        .with(httpBasic("jsmith", "password123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jwtRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwttoken").isNotEmpty());
    }
}