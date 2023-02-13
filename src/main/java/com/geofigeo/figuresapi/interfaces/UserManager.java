package com.geofigeo.figuresapi.interfaces;

import com.geofigeo.figuresapi.dtos.SignUpRequestDto;
import com.geofigeo.figuresapi.dtos.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserManager {
    void createUser(SignUpRequestDto signUpDto);
    Page<UserDto> getAllUsers(Pageable pageable);
}
