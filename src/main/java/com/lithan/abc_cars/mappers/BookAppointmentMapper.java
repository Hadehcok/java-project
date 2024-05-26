package com.lithan.abc_cars.mappers;

import com.lithan.abc_cars.dtos.BookAppointmentDto;
import com.lithan.abc_cars.models.BookAppointment;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class BookAppointmentMapper {
    public static BookAppointment mapToBookOppointment(BookAppointmentDto bookOppointmentDto) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate appointmentDate = LocalDate.parse(bookOppointmentDto.getAppointmentDateTime(), formatter);

        return BookAppointment.builder()
                .id(bookOppointmentDto.getId())
                .user(bookOppointmentDto.getUser())
                .car(bookOppointmentDto.getCar())
                .address(bookOppointmentDto.getAddress())
                .message(bookOppointmentDto.getMessage())
                .appointmentDateTime(appointmentDate)
                .status(bookOppointmentDto.isStatus())
                .build();
    }

    public static BookAppointmentDto mapToBookOppointmentDto(BookAppointment bookOppointment) throws ParseException {

        return BookAppointmentDto.builder()
                .id(bookOppointment.getId())
                .user(bookOppointment.getUser())
                .car(bookOppointment.getCar())
                .address(bookOppointment.getAddress())
                .message(bookOppointment.getMessage())
                .appointmentDateTime(bookOppointment.getAppointmentDateTime().toString())
                .status(bookOppointment.isStatus())
                .build();
    }
}
