package com.geofigeo.figuresapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtRequestDto {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
}
