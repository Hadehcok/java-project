package com.lithan.abc_cars.dtos;

import lombok.Data;

@Data
public class PasswordDto {

    private String oldPassword;

    private String newPassword;
}