package com.geofigeo.figuresapi.controller;

import com.geofigeo.figuresapi.dto.JwtRequestDto;
import com.geofigeo.figuresapi.dto.JwtResponseDto;
import com.geofigeo.figuresapi.security.jwt.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class JwtAuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/api/v1/authenticate")
    public ResponseEntity<JwtResponseDto> createAuthenticationToken(@RequestBody JwtRequestDto authenticationRequest) throws Exception {
        final JwtResponseDto jwtResponse = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}
