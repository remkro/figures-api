package com.geofigeo.figuresapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geofigeo.figuresapi.FiguresApiApplication;
import com.geofigeo.figuresapi.dto.AddShapeRequestDto;
import com.geofigeo.figuresapi.dto.JwtRequestDto;
import com.geofigeo.figuresapi.dto.SignUpRequestDto;
import com.geofigeo.figuresapi.repository.ShapeRepository;
import com.geofigeo.figuresapi.repository.UserRepository;
import com.geofigeo.figuresapi.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FiguresApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class FigureControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    ShapeRepository shapeRepository;
    @Autowired
    UserRepository userRepository;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        shapeRepository.deleteAll();
    }

    @Test
    void should_add_circle() throws Exception {
        // Add user
        SignUpRequestDto signUpRequestDto = TestUtils.createSignUpRequest();
        String signUpRequestDtoAsString = mapper.writeValueAsString(signUpRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestDtoAsString))
                .andDo(print());

        // Get JWT token with created user credentials
        JwtRequestDto jwtRequestDto = new JwtRequestDto("jsmith", "password123");
        String jwtRequestDtoAsString = mapper.writeValueAsString(jwtRequestDto);

        MvcResult jwtRequestResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authenticate")
                        .with(httpBasic("jsmith", "password123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jwtRequestDtoAsString))
                .andDo(print())
                .andReturn();

        String token = TestUtils.getTokenFromJson(jwtRequestResponse.getResponse().getContentAsString());

        // Send request to add circle
        AddShapeRequestDto addShapeRequestDto = new AddShapeRequestDto("CIRCLE", List.of(5.0));
        String addShapeRequestDtoAsString = mapper.writeValueAsString(addShapeRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addShapeRequestDtoAsString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("CIRCLE"))
                .andExpect(jsonPath("$.createdBy").value("jsmith"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedBy").value("jsmith"))
                .andExpect(jsonPath("$.area").value(78.53981633974483))
                .andExpect(jsonPath("$.perimeter").value(31.41592653589793))
                .andExpect(jsonPath("$.radius").value(5.0));
    }

    @Test
    void should_fail_to_add_shape_when_not_supported() throws Exception {
        // Add user
        SignUpRequestDto signUpRequestDto = TestUtils.createSignUpRequest();
        String signUpRequestDtoAsString = mapper.writeValueAsString(signUpRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestDtoAsString))
                .andDo(print());

        // Get JWT token with created user credentials
        JwtRequestDto jwtRequestDto = new JwtRequestDto("jsmith", "password123");
        String jwtRequestDtoAsString = mapper.writeValueAsString(jwtRequestDto);

        MvcResult jwtRequestResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authenticate")
                        .with(httpBasic("jsmith", "password123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jwtRequestDtoAsString))
                .andDo(print())
                .andReturn();

        String token = TestUtils.getTokenFromJson(jwtRequestResponse.getResponse().getContentAsString());
        System.out.println(token);

        // Send request to add shape
        AddShapeRequestDto addShapeRequestDto = new AddShapeRequestDto("TRIANGLE", List.of(5.0, 3.0, 2.0));
        String addShapeRequestDtoAsString = mapper.writeValueAsString(addShapeRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addShapeRequestDtoAsString))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("HANDLER_NOT_FOUND"));
    }
}