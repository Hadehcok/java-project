package com.lithan.abc_cars.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lithan.abc_cars.dtos.RegistrationDto;
import com.lithan.abc_cars.dtos.UpdateUserDto;
import com.lithan.abc_cars.models.Role;
import com.lithan.abc_cars.models.UserEntity;
import com.lithan.abc_cars.repositories.RoleRepository;
import com.lithan.abc_cars.repositories.UserRepository;
import com.lithan.abc_cars.services.UserService;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        UserEntity user = new UserEntity();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEmail(registrationDto.getEmail());
        user.setAddress(registrationDto.getAddress());
        Role role = roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updateProfile(UserEntity user, UpdateUserDto updateUserDto) {
        // Retrieve the user entity from the database

        // Update the user entity with the new profile information
        user.setUsername(updateUserDto.getUsername());
        user.setEmail(updateUserDto.getEmail());
        user.setAddress(updateUserDto.getAddress());
        // Save the updated user entity back to the database
        userRepository.save(user);
    }

    @Override
    public void updatePassword(UserEntity user, String oldPassword, String newPassword) {

        // Verify the old password
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            // Encode the new password
            String encodedNewPassword = passwordEncoder.encode(newPassword);

            // Update the user's password with the new encoded password
            user.setPassword(encodedNewPassword);

            // Save the updated user entity back to the database
            userRepository.save(user);
        } else {
            // If the old password doesn't match, throw an exception or return an error
            // message
            throw new IllegalArgumentException("Incorrect old password");
        }
    }

    @Override
    public void setAsAdmin(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(user -> {
            // Fetch the admin role from the database
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            // Clear existing roles and assign admin role to the user
            user.getRoles().clear();
            user.getRoles().add(adminRole);
            // Save the updated user with admin role
            userRepository.save(user);
        });
    }


}
