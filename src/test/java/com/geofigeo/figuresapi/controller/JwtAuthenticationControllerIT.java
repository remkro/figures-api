package com.geofigeo.figuresapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geofigeo.figuresapi.FiguresApiApplication;
import com.geofigeo.figuresapi.repository.UserRepository;
import com.geofigeo.figuresapi.utils.TestUtils;
import com.geofigeo.figuresapi.dto.JwtRequestDto;
import com.geofigeo.figuresapi.dto.SignUpRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FiguresApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class JwtAuthenticationControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
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