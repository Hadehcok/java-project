package com.lithan.abc_cars.mappers;

import com.lithan.abc_cars.dtos.CarsDto;
import com.lithan.abc_cars.models.Cars;

public class CarMapper {
    public static Cars mapToCar(CarsDto carsDto){
        return Cars.builder()
                .id(carsDto.getId())
                .name(carsDto.getName())
                .description((carsDto.getDescription()))
                .status(carsDto.getStatus())
                .price(carsDto.getPrice())
                .model(carsDto.getModel())
                .year(carsDto.getYear())
                .createdBy(carsDto.getCreatedBy())
                .build();
    }

    public static CarsDto mapToCarDto(Cars cars){
        return CarsDto.builder()
                .id(cars.getId())
                .name(cars.getName())
                .description((cars.getDescription()))
                .status(cars.getStatus())
                .price(cars.getPrice())
                .model(cars.getModel())
                .year(cars.getYear())
                .createdBy(cars.getCreatedBy())
                .build();
    }
}
