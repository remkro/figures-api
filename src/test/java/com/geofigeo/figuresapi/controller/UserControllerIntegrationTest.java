package com.geofigeo.figuresapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geofigeo.figuresapi.FiguresApiApplication;
import com.geofigeo.figuresapi.dto.SignUpRequestDto;
import com.geofigeo.figuresapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FiguresApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserRepository userRepository;

    @Test
    void should_add_user() throws Exception {
        SignUpRequestDto request = createSignUpRequest();
        String addUserRequest = mapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addUserRequest))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("User " + request.getUsername()
                        + " registered successfully"));
    }

    @Test
    void should_failing_adding_user_when_same_username_exists() throws Exception {
        SignUpRequestDto request = createSignUpRequest();
        String addUserRequest = mapper.writeValueAsString(request);

        Mockito.when(userRepository.existsWithLockingByUsername(request.getUsername())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addUserRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("USERNAME_ALREADY_TAKEN"));
    }

    private SignUpRequestDto createSignUpRequest() {
        SignUpRequestDto request = new SignUpRequestDto();
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setUsername("jsmith");
        request.setEmail("jsmith@gmail.com");
        request.setPassword("password123");
        return request;
    }
}