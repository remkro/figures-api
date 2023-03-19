package com.geofigeo.figuresapi.controller;

import com.geofigeo.figuresapi.BaseIntegrationTest;
import com.geofigeo.figuresapi.dto.SignUpRequestDto;
import com.geofigeo.figuresapi.repository.UserRepository;
import com.geofigeo.figuresapi.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIntegrationTest extends BaseIntegrationTest {
    @MockBean
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    void should_add_user() throws Exception {
        // Create sign up request DTO
        SignUpRequestDto signUpRequestDto = TestUtils.createSignUpRequest();
        String signUpRequestDtoAsString = mapper.writeValueAsString(signUpRequestDto);

        // Send sign up request and confirm user creation
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestDtoAsString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("User " + signUpRequestDto.getUsername()
                        + " registered successfully"));
    }

    @Test
    void should_failing_adding_user_when_same_username_exists() throws Exception {
        // Create sign up request DTO
        SignUpRequestDto signUpRequestDto = TestUtils.createSignUpRequest();
        String signUpRequestDtoAsString = mapper.writeValueAsString(signUpRequestDto);

        // Fake behaviour that user already exists
        Mockito.when(userRepository.existsWithLockingByUsername(signUpRequestDto.getUsername())).thenReturn(true);

        // Send sign up request and confirm failing to add user with username that already exists
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestDtoAsString))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("USERNAME_ALREADY_TAKEN"));
    }
}