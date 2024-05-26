package com.lithan.abc_cars.dtos;

import com.lithan.abc_cars.models.Cars;
import com.lithan.abc_cars.models.UserEntity;
import lombok.*;

import java.util.Date;

@Data
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BiddingDto {
    private Long id;
    private UserEntity userId;
    private Cars carId;
    private double biddingPrice;
    private Date timestamp;
    private boolean accepted;
}
