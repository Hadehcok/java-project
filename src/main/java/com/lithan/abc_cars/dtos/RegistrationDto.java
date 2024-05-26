package com.lithan.abc_cars.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrationDto {
    private Long id;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String address;
}
