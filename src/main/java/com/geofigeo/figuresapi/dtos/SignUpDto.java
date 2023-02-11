package com.geofigeo.figuresapi.dtos;

import lombok.Data;

@Data
public class SignUpDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
}