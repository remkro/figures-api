package com.geofigeo.figuresapi.abstraction;

import com.geofigeo.figuresapi.dto.SignUpRequestDto;
import com.geofigeo.figuresapi.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserManager {
    void createUser(SignUpRequestDto signUpDto);
    Page<UserDto> getAllUsers(Pageable pageable);
}
