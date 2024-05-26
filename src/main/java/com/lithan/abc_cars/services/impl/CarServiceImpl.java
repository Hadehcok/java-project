package com.lithan.abc_cars.services.impl;

import com.lithan.abc_cars.dtos.CarsDto;
import com.lithan.abc_cars.mappers.CarMapper;
import com.lithan.abc_cars.models.Cars;
import com.lithan.abc_cars.models.UserEntity;
import com.lithan.abc_cars.repositories.CarRepository;
import com.lithan.abc_cars.repositories.UserRepository;
import com.lithan.abc_cars.security.SecurityUtil;
import com.lithan.abc_cars.services.CarService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.lithan.abc_cars.mappers.CarMapper.mapToCar;
import static com.lithan.abc_cars.mappers.CarMapper.mapToCarDto;

@Service
public class CarServiceImpl implements CarService {
    private final UserRepository userRepository;
    private CarRepository carRepository;

    public CarServiceImpl(UserRepository userRepository, CarRepository carRepository) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }



    @Override
    public Cars addCar(CarsDto carsDto) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);
        MultipartFile image = carsDto.getImage();
        Date createdDate = new Date();
        String storageFileName = createdDate + "_" + image.getOriginalFilename();

        // Upload image process
        try {
            String uploadDir = "public/images";
            Path uploadPath = Paths.get(uploadDir);

            // If directory does not exist, create the directory
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                // Concatenate the upload directory and file name with a file separator
                Files.copy(inputStream, Paths.get(uploadDir, storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (Exception exception) {
            System.out.println("Exception: " + exception);
        }

        Cars car = mapToCar(carsDto);
        car.setImage(storageFileName);
        car.setCreatedBy(user);

        return carRepository.save(car);
    }

    @Override
    public void updateCar(CarsDto carsDto, MultipartFile image) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);

        // Set the createdBy user
        Cars cars = mapToCar(carsDto);
        cars.setCreatedBy(user);


        // Update the image filename
        if (!carsDto.getImage().isEmpty()){
            // Delete old image
            String uploadDir = "public/images/";
            Path oldImagePath = Paths.get(uploadDir + cars.getImage());

            try {
                // Check if the old image file exists before attempting to delete it
                if (Files.exists(oldImagePath)) {
                    Files.delete(oldImagePath);
                    System.out.println("Old image deleted successfully: " + oldImagePath);
                } else {
                    System.out.println("Old image does not exist: " + oldImagePath);
                }
            } catch (IOException exception) {
                System.out.println("Error deleting old image: " + exception.getMessage());
            }

            // Save new image
            Date createdAt = new Date();
            String storageFilename = createdAt.getTime() + "_" + image.getOriginalFilename();

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFilename), StandardCopyOption.REPLACE_EXISTING);
                // If no new image is provided, retain the existing image filename
                // Check if the CarsDto contains the image filename
                if (carsDto.getImage() != null && !carsDto.getImage().isEmpty()) {
                    // If the CarsDto contains the image filename, set it to the Cars entity
                    cars.setImage(carsDto.getImage().getOriginalFilename());
                }
                cars.setImage(storageFilename);
            } catch (IOException exception) {
                System.out.println("Error saving new image: " + exception.getMessage());
            }
        }

        // Save the updated car
        carRepository.save(cars);
    }

    @Override
    public void deleteCar(Long carId, Cars cars) {
        String image = cars.getImage();

            // Delete old image
            String uploadDir = "public/images/";
            Path oldImagePath = Paths.get(uploadDir + cars.getImage());

            try {
                // Check if the old image file exists before attempting to delete it
                if (Files.exists(oldImagePath)) {
                    Files.delete(oldImagePath);
                    System.out.println("Old image deleted successfully: " + oldImagePath);
                } else {
                    System.out.println("Old image does not exist: " + oldImagePath);
                }
            } catch (IOException exception) {
                System.out.println("Error deleting old image: " + exception.getMessage());
            }

            carRepository.deleteById(carId);
    }



}
