package com.geofigeo.figuresapi.dto;

import lombok.Data;

@Data
public class JwtRequestDto {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
}
