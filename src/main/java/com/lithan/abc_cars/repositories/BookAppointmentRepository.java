package com.lithan.abc_cars.repositories;

import com.lithan.abc_cars.models.BookAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAppointmentRepository extends JpaRepository<BookAppointment, Long> {
}
