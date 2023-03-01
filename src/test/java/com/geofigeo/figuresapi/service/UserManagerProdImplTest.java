package com.geofigeo.figuresapi.service;

import com.geofigeo.figuresapi.dto.SignUpRequestDto;
import com.geofigeo.figuresapi.entity.Role;
import com.geofigeo.figuresapi.entity.User;
import com.geofigeo.figuresapi.exception.EmailAlreadyTakenException;
import com.geofigeo.figuresapi.exception.UserAlreadyTakenException;
import com.geofigeo.figuresapi.repository.RoleRepository;
import com.geofigeo.figuresapi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class UserManagerProdImplTest {
    @InjectMocks
    private UserManagerProdImpl userManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void should_add_user() {
        // Create test data
        SignUpRequestDto signUpDto = new SignUpRequestDto();
        signUpDto.setUsername("testuser");
        signUpDto.setEmail("testuser@example.com");
        signUpDto.setFirstName("Thomas");
        signUpDto.setLastName("Anderson");
        signUpDto.setPassword("testpassword");

        Role creatorRole = new Role();
        creatorRole.setName("CREATOR");

        // Mock repositories to return specific values
        Mockito.when(roleRepository.findByName(anyString())).thenReturn(creatorRole);
        Mockito.when(userRepository.existsWithLockingByUsername(anyString())).thenReturn(false);
        Mockito.when(userRepository.existsWithLockingByEmail(anyString())).thenReturn(false);

        User preparedUser = new User();
        preparedUser.setUsername("testuser");
        preparedUser.setFirstName("Thomas");
        preparedUser.setLastName("Anderson");
        preparedUser.setEmail("testuser@example.com");
        preparedUser.addRole(creatorRole);
        preparedUser.setPassword("encodedPassword");
        ;
        Mockito.when(userRepository.save(any(User.class))).thenReturn(preparedUser);

        // Call the createUser() method
        User addedUser = userManager.createUser(signUpDto);

        // Verify results
        assertNotNull(addedUser);
        assertEquals(preparedUser.getUsername(), addedUser.getUsername());
        assertEquals(preparedUser.getEmail(), addedUser.getEmail());
        assertEquals(preparedUser.getFirstName(), addedUser.getFirstName());
        assertEquals(preparedUser.getLastName(), addedUser.getLastName());
        assertEquals(preparedUser.getPassword(), addedUser.getPassword());
        assertEquals(1, addedUser.getRoles().size());
    }

    @Test
    public void should_throw_exception_when_username_exists() {
        // Create test data
        String username = "testuser";
        String email = "testuser@example.com";
        SignUpRequestDto signUpDto = new SignUpRequestDto();
        signUpDto.setFirstName("Test");
        signUpDto.setLastName("User");
        signUpDto.setUsername(username);
        signUpDto.setEmail(email);
        signUpDto.setPassword("testpassword");

        // Mock repository to return true to pretend that username already exists
        Mockito.when(userRepository.existsWithLockingByUsername(anyString())).thenReturn(true);

        // Call the createUser() method and expect an exception to be thrown
        UserAlreadyTakenException thrown = Assertions.assertThrows(UserAlreadyTakenException.class, () -> {
            userManager.createUser(signUpDto);
        });
    }

    @Test
    public void should_throw_exception_when_email_is_taken() {
        // Create test data
        String username = "testuser";
        String email = "testuser@example.com";
        SignUpRequestDto signUpDto = new SignUpRequestDto();
        signUpDto.setFirstName("Test");
        signUpDto.setLastName("User");
        signUpDto.setUsername(username);
        signUpDto.setEmail(email);
        signUpDto.setPassword("testpassword");

        // Mock repository to return true to pretend that email already exists
        Mockito.when(userRepository.existsWithLockingByEmail(email)).thenReturn(true);

        // Call the createUser() method and expect an exception to be thrown
        EmailAlreadyTakenException thrown = Assertions.assertThrows(EmailAlreadyTakenException.class, () -> {
            userManager.createUser(signUpDto);
        });
    }
}