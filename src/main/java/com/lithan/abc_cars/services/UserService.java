package com.lithan.abc_cars.services;

import com.lithan.abc_cars.dtos.RegistrationDto;
import com.lithan.abc_cars.dtos.UpdateUserDto;
import com.lithan.abc_cars.models.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public void saveUser(RegistrationDto registrationDto);

    public UserEntity findByUsername(String username);

    public UserEntity findByEmail(String email);

    void updateProfile(UserEntity user, UpdateUserDto updateUserDto);

    void updatePassword(UserEntity username, String oldPassword, String newPassword);

    void setAsAdmin(Long userId);

}
