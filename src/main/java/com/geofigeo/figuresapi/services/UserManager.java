package com.geofigeo.figuresapi.services;

import com.geofigeo.figuresapi.dtos.SignUpDto;
import com.geofigeo.figuresapi.dtos.UserDto;
import com.geofigeo.figuresapi.entities.Role;
import com.geofigeo.figuresapi.entities.User;
import com.geofigeo.figuresapi.exceptions.EmailAlreadyTakenException;
import com.geofigeo.figuresapi.exceptions.UserAlreadyTakenException;
import com.geofigeo.figuresapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserManager {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public void createUser(SignUpDto signUpDto) {
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            throw new UserAlreadyTakenException("Username is already taken!");
        }
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new EmailAlreadyTakenException("Email is already taken!");
        }

        User user = new User();
        user.setFirstName(signUpDto.getFirstName());
        user.setLastName(signUpDto.getLastName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.addRole(new Role("CREATOR"));

        userRepository.save(user);
    }

    public Page<UserDto> getAllUsers(Pageable pageable) {
        Page<User> all = userRepository.findAll(pageable);
        return all.map(this::mapUserToUserDto);
    }

    private UserDto mapUserToUserDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setShapesCreated(user.getShapes().size());
        userDto.setRole(user.getRoles().stream().map(Role::getName).toList());
        return userDto;
    }
}
