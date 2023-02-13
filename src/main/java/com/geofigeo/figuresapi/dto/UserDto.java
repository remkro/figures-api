package com.geofigeo.figuresapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private List<String> role;
    private long shapesCreated;
}
