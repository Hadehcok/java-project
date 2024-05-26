package com.lithan.abc_cars;

import com.lithan.abc_cars.dtos.CarsDto;
import com.lithan.abc_cars.models.Cars;
import com.lithan.abc_cars.models.UserEntity;
import com.lithan.abc_cars.repositories.CarRepository;
import com.lithan.abc_cars.repositories.UserRepository;
import com.lithan.abc_cars.services.CarService;
import com.lithan.abc_cars.services.impl.CarServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;



    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddCar() {
        // Mock data for a car
        Cars mockCar = new Cars();
        mockCar.setId(1L);
        mockCar.setName("Toyota");
        mockCar.setModel("Camry");
        mockCar.setPrice(1000);
        mockCar.setYear(2020);

        // Mock behavior of carRepository.save()
        when(carRepository.save(Mockito.any(Cars.class))).thenReturn(mockCar);

        // Call the method under test
        Cars savedCar = carRepository.save(mockCar);

        // Verify that carRepository.save(...) was invoked with the expected car object
        verify(carRepository).save(Mockito.any(Cars.class));

        // Perform assertions on the returned car object
        assertEquals(mockCar.getId(), savedCar.getId());
        assertEquals(mockCar.getName(), savedCar.getName());
        assertEquals(mockCar.getModel(), savedCar.getModel());
        assertEquals(mockCar.getYear(), savedCar.getYear());
        assertEquals(mockCar.getPrice(), savedCar.getPrice());
    }
}
