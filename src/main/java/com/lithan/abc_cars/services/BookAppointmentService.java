package com.lithan.abc_cars.services;

import com.lithan.abc_cars.dtos.BookAppointmentDto;
import com.lithan.abc_cars.models.BookAppointment;
import com.lithan.abc_cars.models.Cars;

import java.text.ParseException;
import java.util.List;

public interface BookAppointmentService {
    BookAppointment bookAppointment(BookAppointmentDto bookOppointmentDto) throws ParseException;

    List<BookAppointmentDto> getAllAppointments() throws ParseException;

    void confirmAppointment(Long appointmentId);
}

