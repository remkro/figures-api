package com.geofigeo.figuresapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geofigeo.figuresapi.FiguresApiApplication;
import com.geofigeo.figuresapi.dto.AddShapeRequestDto;
import com.geofigeo.figuresapi.dto.EditShapeRequestDto;
import com.geofigeo.figuresapi.dto.JwtRequestDto;
import com.geofigeo.figuresapi.dto.SignUpRequestDto;
import com.geofigeo.figuresapi.dto.SquareDto;
import com.geofigeo.figuresapi.repository.ChangeRepository;
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
    @Autowired
    ChangeRepository changeRepository;

    @AfterEach
    void clean() {
        shapeRepository.deleteAll();
        changeRepository.deleteAll();
        userRepository.deleteAll();
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

        // Send request to add circle and confirm adding
        AddShapeRequestDto addShapeRequestDto = new AddShapeRequestDto("CIRCLE", List.of(5.0));
        String addShapeRequestDtoAsString = mapper.writeValueAsString(addShapeRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addShapeRequestDtoAsString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
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
    void should_add_square() throws Exception {
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

        // Send request to add square and confirm adding
        AddShapeRequestDto addShapeRequestDto = new AddShapeRequestDto("SQUARE", List.of(5.0));
        String addShapeRequestDtoAsString = mapper.writeValueAsString(addShapeRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addShapeRequestDtoAsString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.type").value("SQUARE"))
                .andExpect(jsonPath("$.createdBy").value("jsmith"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedBy").value("jsmith"))
                .andExpect(jsonPath("$.area").value(25.0))
                .andExpect(jsonPath("$.perimeter").value(20.0))
                .andExpect(jsonPath("$.height").value(5.0));
    }

    @Test
    void should_add_rectangle() throws Exception {
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

        // Send request to add square and confirm adding
        AddShapeRequestDto addShapeRequestDto = new AddShapeRequestDto("RECTANGLE", List.of(4.0, 6.0));
        String addShapeRequestDtoAsString = mapper.writeValueAsString(addShapeRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addShapeRequestDtoAsString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.type").value("RECTANGLE"))
                .andExpect(jsonPath("$.createdBy").value("jsmith"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedBy").value("jsmith"))
                .andExpect(jsonPath("$.area").value(24.0))
                .andExpect(jsonPath("$.perimeter").value(20.0))
                .andExpect(jsonPath("$.width").value(4.0))
                .andExpect(jsonPath("$.height").value(6.0));
    }

    @Test
    void should_make_changes_in_shape() throws Exception {
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

        // Send request to add square
        AddShapeRequestDto addShapeRequestDto = new AddShapeRequestDto("SQUARE", List.of(5.0));
        String addShapeRequestDtoAsString = mapper.writeValueAsString(addShapeRequestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addShapeRequestDtoAsString))
                .andDo(print())
                .andReturn();

        SquareDto squareDto = mapper.readValue(result.getResponse().getContentAsString(), SquareDto.class);
        long id = squareDto.getId();

        // Send request to change shape and confirm that change
        EditShapeRequestDto editShapeRequestDto = new EditShapeRequestDto(id, List.of(10.0));
        String editShapeRequestDtoAsString = mapper.writeValueAsString(editShapeRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/shapes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editShapeRequestDtoAsString))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.type").value("SQUARE"))
                .andExpect(jsonPath("$.createdBy").value("jsmith"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedBy").value("jsmith"))
                .andExpect(jsonPath("$.area").value(100.0))
                .andExpect(jsonPath("$.perimeter").value(40.0))
                .andExpect(jsonPath("$.height").value(10.0));
    }

    @Test
    void should_get_changes_of_shape() throws Exception {
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

        // Send request to add square
        AddShapeRequestDto addShapeRequestDto = new AddShapeRequestDto("SQUARE", List.of(5.0));
        String addShapeRequestDtoAsString = mapper.writeValueAsString(addShapeRequestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/shapes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addShapeRequestDtoAsString))
                .andDo(print())
                .andReturn();

        SquareDto squareDto = mapper.readValue(result.getResponse().getContentAsString(), SquareDto.class);
        long id = squareDto.getId();

        // Send request to change shape
        EditShapeRequestDto editShapeRequestDto = new EditShapeRequestDto(id, List.of(10.0));
        String editShapeRequestDtoAsString = mapper.writeValueAsString(editShapeRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/shapes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editShapeRequestDtoAsString))
                .andDo(print());

        // Send request to get these changes and confirm them
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/shapes/" + id + "/changes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editShapeRequestDtoAsString))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].shapeId").value(id))
                .andExpect(jsonPath("$[0].changedValues.oldHeight").value(5.0))
                .andExpect(jsonPath("$[0].changedValues.newHeight").value(10.0));
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

        // Send request to add shape and confirm failing to add shape that is not supported
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