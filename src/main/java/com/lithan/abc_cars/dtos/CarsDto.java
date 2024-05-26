package com.lithan.abc_cars.dtos;

import com.lithan.abc_cars.models.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarsDto {
    private Long id;
    private String name;
    private String model;
    private String description;
    private MultipartFile image;
    private String status;
    private int year;
    private int price;
    private UserEntity createdBy;
}


