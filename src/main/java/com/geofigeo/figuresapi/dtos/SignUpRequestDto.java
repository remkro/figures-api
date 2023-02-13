package com.geofigeo.figuresapi.dtos;

import lombok.Data;

@Data
public class SignUpRequestDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
}
