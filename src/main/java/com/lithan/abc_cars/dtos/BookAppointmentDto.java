package com.lithan.abc_cars.dtos;

import com.lithan.abc_cars.models.Cars;
import com.lithan.abc_cars.models.UserEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class BookAppointmentDto {
    private Long id;

    private Cars car;

    private UserEntity user;

    private String appointmentDateTime;

    private String message;

    private String address;

    private boolean status;

}
