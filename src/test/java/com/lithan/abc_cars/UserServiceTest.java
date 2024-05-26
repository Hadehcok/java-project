package com.lithan.abc_cars;

import com.lithan.abc_cars.dtos.RegistrationDto;
import com.lithan.abc_cars.models.Role;
import com.lithan.abc_cars.models.UserEntity;
import com.lithan.abc_cars.repositories.RoleRepository;
import com.lithan.abc_cars.repositories.UserRepository;
import com.lithan.abc_cars.services.UserService;
import com.lithan.abc_cars.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testSaveUser() {
        // Mock registration data
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setUsername("testuser");
        registrationDto.setPassword("password");
        registrationDto.setEmail("testuser@example.com");
        registrationDto.setAddress("123 Test St");

        // Mock role
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        // Mock behavior of roleRepository.findByName()
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);

        // Mock behavior of passwordEncoder.encode()
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Call the method under test
        userService.saveUser(registrationDto);

        // Verify that userRepository.save(...) was invoked with the expected user
        verify(userRepository).save(any(UserEntity.class));

        // Verify that the user object passed to userRepository.save(...) matches the expected values
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());

        UserEntity capturedUser = userCaptor.getValue();
        assertEquals("testuser", capturedUser.getUsername());
        assertEquals("encodedPassword", capturedUser.getPassword()); // Ensure password is encoded
        assertEquals("testuser@example.com", capturedUser.getEmail());
        assertEquals("123 Test St", capturedUser.getAddress());
        assertEquals(Collections.singletonList(role), capturedUser.getRoles());
    }

    @Test
    public void testSaveUser_withPotentialSqlInjection() {
        // Prepare a RegistrationDto with potentially malicious input
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setUsername("maliciousUser'); DROP TABLE users;--");
        registrationDto.setPassword("password");
        registrationDto.setEmail("malicious@example.com");
        registrationDto.setAddress("123 Malicious St");

        // Mock behavior of roleRepository.findByName()
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);

        // Mock behavior of passwordEncoder.encode()
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Call the method under test
        userService.saveUser(registrationDto);

        // Verify that userRepository.save(...) was invoked with the expected user
        verify(userRepository).save(any(UserEntity.class));

        // Capture the UserEntity passed to userRepository.save(...)
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());

        UserEntity capturedUser = userCaptor.getValue();

        // Assert that the user details were saved correctly
        assertEquals("maliciousUser'); DROP TABLE users;--", capturedUser.getUsername());
        assertEquals("encodedPassword", capturedUser.getPassword()); // Ensure password is encoded
        assertEquals("malicious@example.com", capturedUser.getEmail());
        assertEquals("123 Malicious St", capturedUser.getAddress());
        assertEquals(Collections.singletonList(role), capturedUser.getRoles());

        // Assert that userRepository.save(...) was only called once
        verify(userRepository, times(1)).save(any(UserEntity.class));

        // Add more assertions as needed to verify the correct behavior and no SQL injection occurred
    }
}
