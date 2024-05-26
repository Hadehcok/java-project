package com.lithan.abc_cars.services.impl;

import com.lithan.abc_cars.dtos.BookAppointmentDto;
import com.lithan.abc_cars.models.BookAppointment;
import com.lithan.abc_cars.models.Cars;
import com.lithan.abc_cars.models.UserEntity;
import com.lithan.abc_cars.repositories.BookAppointmentRepository;
import com.lithan.abc_cars.repositories.CarRepository;
import com.lithan.abc_cars.repositories.UserRepository;
import com.lithan.abc_cars.security.SecurityUtil;
import com.lithan.abc_cars.services.BookAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.lithan.abc_cars.mappers.BookAppointmentMapper.mapToBookOppointment;
import static com.lithan.abc_cars.mappers.BookAppointmentMapper.mapToBookOppointmentDto;

@Service
public class BookAppointmentServiceImpl implements BookAppointmentService {
    private CarRepository carRepository;
    private BookAppointmentRepository bookOppointmentRepository;
    private UserRepository userRepository;

    @Autowired
    public BookAppointmentServiceImpl(CarRepository carRepository, BookAppointmentRepository bookOppointmentRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.bookOppointmentRepository = bookOppointmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BookAppointment bookAppointment(BookAppointmentDto bookOppointmentDto) throws ParseException {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);

        BookAppointment bookOppointment = mapToBookOppointment(bookOppointmentDto);



        bookOppointmentRepository.save(bookOppointment);
        return bookOppointment;
    }

    @Override
    public List<BookAppointmentDto> getAllAppointments() throws ParseException {
        List<BookAppointment> appointments = bookOppointmentRepository.findAll();
        List<BookAppointmentDto> appointmentDtos = new ArrayList<>();

        for (BookAppointment appointment : appointments) {
            appointmentDtos.add(mapToBookOppointmentDto(appointment));
        }


        return appointmentDtos;
    }

    @Override
    public void confirmAppointment(Long appointmentId) {
        BookAppointment appointment = bookOppointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        appointment.setStatus(true);
        bookOppointmentRepository.save(appointment);
    }
}
