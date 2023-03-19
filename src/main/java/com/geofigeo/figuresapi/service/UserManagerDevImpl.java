package com.geofigeo.figuresapi.service;

import com.geofigeo.figuresapi.dto.SignUpRequestDto;
import com.geofigeo.figuresapi.dto.UserDto;
import com.geofigeo.figuresapi.entity.Role;
import com.geofigeo.figuresapi.entity.User;
import com.geofigeo.figuresapi.exception.EmailAlreadyTakenException;
import com.geofigeo.figuresapi.exception.UserAlreadyTakenException;
import com.geofigeo.figuresapi.abstraction.UserManager;
import com.geofigeo.figuresapi.repository.RoleRepository;
import com.geofigeo.figuresapi.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile({"dev", "test"})
@Component
@RequiredArgsConstructor
public class UserManagerDevImpl implements UserManager {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @PostConstruct
    private void initRoles() {
        roleRepository.save(new Role("CREATOR"));
        roleRepository.save(new Role("ADMIN"));
    }

    @Transactional
    public User createUser(SignUpRequestDto signUpDto) {
        if (userRepository.existsWithLockingByUsername(signUpDto.getUsername())) {
            throw new UserAlreadyTakenException("USERNAME_ALREADY_TAKEN");
        }
        if (userRepository.existsWithLockingByEmail(signUpDto.getEmail())) {
            throw new EmailAlreadyTakenException("EMAIL_ALREADY_TAKEN");
        }

        User user = new User();
        user.setFirstName(signUpDto.getFirstName());
        user.setLastName(signUpDto.getLastName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.addRole(roleRepository.findByName("CREATOR"));

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::mapUserToUserDto);
    }

    private UserDto mapUserToUserDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setShapesCreated(user.getShapes().size());
        userDto.setRole(user.getRoles().stream().map(Role::getName).toList());
        return userDto;
    }
}
