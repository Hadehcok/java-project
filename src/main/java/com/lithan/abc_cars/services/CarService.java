package com.lithan.abc_cars.services;

import com.lithan.abc_cars.dtos.CarsDto;
import com.lithan.abc_cars.models.Cars;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CarService {
    Cars addCar(CarsDto car);
    void updateCar(CarsDto carsDto, MultipartFile image);
    void deleteCar(Long carId, Cars cars);
}
