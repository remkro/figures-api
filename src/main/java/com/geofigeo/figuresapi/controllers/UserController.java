package com.geofigeo.figuresapi.controllers;

import com.geofigeo.figuresapi.dtos.LoginRequestDto;
import com.geofigeo.figuresapi.dtos.SignUpRequestDto;
import com.geofigeo.figuresapi.dtos.StatusDto;
import com.geofigeo.figuresapi.dtos.UserDto;
import com.geofigeo.figuresapi.security.AuthenticationService;
import com.geofigeo.figuresapi.services.UserManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService authenticationService;
    private final UserManager userManager;

    @PostMapping("/signin")
    public ResponseEntity<StatusDto> authenticateUser(@RequestBody LoginRequestDto loginDto) {
        authenticationService.authenticate(loginDto);
        return ResponseEntity.ok(new StatusDto("User " + loginDto.getUsername() + " signed-in successfully!"));
    }

    @PostMapping("/register")
    public ResponseEntity<StatusDto> registerUser(@RequestBody SignUpRequestDto signUpDto) {
        userManager.createUser(signUpDto);
        return ResponseEntity.ok(new StatusDto("User " + signUpDto.getUsername() + " registered successfully"));
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userManager.getAllUsers(pageable));
    }
}
