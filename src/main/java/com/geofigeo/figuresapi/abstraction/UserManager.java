package com.geofigeo.figuresapi.abstraction;

import com.geofigeo.figuresapi.dto.SignUpRequestDto;
import com.geofigeo.figuresapi.dto.UserDto;
import com.geofigeo.figuresapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserManager {
    User createUser(SignUpRequestDto signUpDto);
    Page<UserDto> getAllUsers(Pageable pageable);
}
