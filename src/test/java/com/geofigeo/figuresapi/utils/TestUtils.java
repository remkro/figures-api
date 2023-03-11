package com.geofigeo.figuresapi.utils;

import com.geofigeo.figuresapi.dto.SignUpRequestDto;

public class TestUtils {
    public static SignUpRequestDto createSignUpRequest() {
        SignUpRequestDto request = new SignUpRequestDto();
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setUsername("jsmith");
        request.setEmail("jsmith@gmail.com");
        request.setPassword("password123");
        return request;
    }
}
