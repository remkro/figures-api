package com.geofigeo.figuresapi.dtos;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String username;
    private String password;
}
