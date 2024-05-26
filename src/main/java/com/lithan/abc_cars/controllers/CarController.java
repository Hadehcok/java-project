package com.lithan.abc_cars.controllers;

import com.lithan.abc_cars.dtos.CarsDto;
import com.lithan.abc_cars.models.Cars;
import com.lithan.abc_cars.models.UserEntity;
import com.lithan.abc_cars.repositories.CarRepository;
import com.lithan.abc_cars.security.SecurityUtil;
import com.lithan.abc_cars.services.CarService;
import com.lithan.abc_cars.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class CarController {
    private CarService carService;
    private UserService userService;
    private CarRepository carRepository;

    @Autowired
    public CarController(CarService carService, UserService userService, CarRepository carRepository) {
        this.carService = carService;
        this.userService = userService;
        this.carRepository = carRepository;
    }

    @GetMapping("/")
    public String carPage(Model model){

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/login";
        }
        List<Cars> cars = carRepository.findByStatus("active");
        model.addAttribute("user", user);
        model.addAttribute("cars", cars);
        return "cars/carList";
    }

    @GetMapping("/myCar")
    public String myCarPage(Model model){
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/login";
        }
        List<Cars> cars = carRepository.findByCreatedBy(user);
        model.addAttribute("user", user);
        model.addAttribute("cars", cars);
        return "cars/myCarList";
    }

    @GetMapping("/search")
    public String searchCars(@RequestParam(required = false) String makeModel,
                             @RequestParam(required = false) Integer year,
                             @RequestParam(required = false) String price,
                             Model model) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/login";
        }

        if (makeModel != null && !makeModel.isEmpty()) {
            List<Cars> carsByMake = carRepository.searchCarsName(makeModel);
            if (!carsByMake.isEmpty()) {
                model.addAttribute("cars", carsByMake);
            } else {
                List<Cars> carsByModel = carRepository.searchCarsModel(makeModel);
                if (!carsByModel.isEmpty()) {
                    model.addAttribute("cars", carsByModel);
                } else {
                    // Tidak ada mobil yang ditemukan
                    return "redirect:/?fail";
                }
            }
        } else if (year != null) {
            List<Cars> carsByYear = carRepository.findByYear(year);
            model.addAttribute("cars", carsByYear);
        } else if (price != null && !price.isEmpty()) {
            String[] priceRange = price.split("-");
            int minPrice = Integer.parseInt(priceRange[0]);
            int maxPrice = Integer.parseInt(priceRange[1]);
            List<Cars> carsByPrice = carRepository.findByPriceBetween(minPrice, maxPrice);
            model.addAttribute("cars", carsByPrice);
        } else {
            return "redirect:/?fail";
        }

        model.addAttribute("user", user);
        return "cars/carList";
    }
    @GetMapping("/addCar")
    public String addCarPage(Model model){
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("car", new Cars());
        return "cars/addCar";
    }

    @GetMapping("/editCar/{id}")
    public String editCarPage(@PathVariable Long id, Model model) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/login";
        }
        Cars car = carRepository.findById(id).get();
        model.addAttribute("car", car);
        return "cars/editCar";
    }

    @GetMapping("/carDetail/{id}")
    public String carDetailPage(@PathVariable Long id, Model model) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/login";
        }
        Cars car = carRepository.findById(id).get();
        model.addAttribute("user",user);
        model.addAttribute("car", car);
        return "cars/carDetail";
    }

    @GetMapping("/deleteCar/{id}")
    public String deleteCar(@PathVariable Long id) {
        try {
            Cars car = carRepository.findById(id).get();
            carService.deleteCar(id,car);
            System.out.println("Car deleted successfully");
        } catch (Exception ex) {
            System.out.println("Error deleting car: " + ex.getMessage());
        }
        return "redirect:/myCar";
    }

    @PostMapping("/addCar")
    public String addCar(@Valid @ModelAttribute CarsDto carsDto, BindingResult result, Model model, @RequestParam("image") MultipartFile image){
        if (result.hasErrors()){
            model.addAttribute("car", new Cars());
            return "cars/addCar";
        }
        if (image.isEmpty()) {
            result.addError(new FieldError("carsDto", "image", "The image file is required"));
            model.addAttribute("car", new Cars());
            return "cars/addCar";
        }

        carsDto.setImage(image);
        carService.addCar(carsDto);
        System.out.println("successfully saved car");
        return "redirect:/myCar";
    }

    @PostMapping("/editCar/{id}")
    public String updateCar(@PathVariable Long id, @Valid @ModelAttribute CarsDto carsDto, Model model, BindingResult result, @RequestParam("image") MultipartFile image){
        try {
            Cars car = carRepository.findById(id).get();
            model.addAttribute("car", car);
            if (result.hasErrors()){
                model.addAttribute("car", car);
                return "cars/editCar";
            }

            carsDto.setId(id);
            carService.updateCar(carsDto, image);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        System.out.println("Image: "+carsDto.getImage().getOriginalFilename());
        System.out.println("successfully update car");

        return "redirect:/myCar";
    }
}
