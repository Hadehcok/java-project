package com.lithan.abc_cars.controllers;

import com.lithan.abc_cars.dtos.BookAppointmentDto;
import com.lithan.abc_cars.models.Bidding;
import com.lithan.abc_cars.models.BookAppointment;
import com.lithan.abc_cars.models.Cars;
import com.lithan.abc_cars.models.UserEntity;
import com.lithan.abc_cars.repositories.CarRepository;
import com.lithan.abc_cars.security.SecurityUtil;
import com.lithan.abc_cars.services.BookAppointmentService;
import com.lithan.abc_cars.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
public class BookAppointmentController {
    private CarRepository carRepository;
    private UserService userService;
    private BookAppointmentService bookAppointmentService;

    public BookAppointmentController(CarRepository carRepository, UserService userService, BookAppointmentService bookAppointmentService) {
        this.carRepository = carRepository;
        this.userService = userService;
        this.bookAppointmentService = bookAppointmentService;
    }

    @GetMapping("/appointments")
    public String viewAppointments(Model model) throws ParseException {
        List<BookAppointmentDto> appointments = bookAppointmentService.getAllAppointments();
        model.addAttribute("appointments", appointments);
        return "bookAppointment/appointmentList";
    }

    @GetMapping("bookAppointment/{carId}")
    public String addBiddingForm(@PathVariable("carId") Long carId, Model model){
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("carId",carId);
        model.addAttribute("appointmentDto", new BookAppointment());

        return "bookAppointment/form";
    }

    @PostMapping("/bookAppointment/{carId}")
    public String bookAppointment(@PathVariable("carId") Long carId,
                                  @ModelAttribute("appointmentDto") BookAppointmentDto appointmentDto,
                                  BindingResult result,
                                  Model model) throws ParseException {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        Cars car = carRepository.findById(carId).get();

        if (result.hasErrors()) {
            model.addAttribute("carId", carId);

            return "bookAppointment/form";
        }

        if (user == null) {
            return "redirect:/login";
        }

        appointmentDto.setCar(car);
        appointmentDto.setUser(user);
        // Call the service method to save the appointment
        bookAppointmentService.bookAppointment(appointmentDto);

        return "redirect:/carDetail/" + carId + "?book";
    }

    @PostMapping("/appointments/{appointmentId}/confirm")
    public String confirmAppointment(@PathVariable Long appointmentId, Model model) throws ParseException {
        bookAppointmentService.confirmAppointment(appointmentId);

        List<BookAppointmentDto> appointments = bookAppointmentService.getAllAppointments();
        model.addAttribute("appointments", appointments);
        return "redirect:/appointments?success";
    }

}
