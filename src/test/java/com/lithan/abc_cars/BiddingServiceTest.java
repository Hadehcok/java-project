package com.lithan.abc_cars;

import com.lithan.abc_cars.dtos.BiddingDto;
import com.lithan.abc_cars.models.Bidding;
import com.lithan.abc_cars.models.Cars;
import com.lithan.abc_cars.models.UserEntity;
import com.lithan.abc_cars.repositories.BiddingRepository;
import com.lithan.abc_cars.repositories.CarRepository;
import com.lithan.abc_cars.repositories.UserRepository;
import com.lithan.abc_cars.security.SecurityUtil;
import com.lithan.abc_cars.services.impl.BiddingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
public class BiddingServiceTest {
    @Mock
    private BiddingRepository biddingRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddBid() {
        // Mock car and user
        Cars mockCar = new Cars();
        mockCar.setId(1L);

        UserEntity mockUser = new UserEntity();
        mockUser.setUsername("' OR 1=1 --");

        // Create a Bidding object with malicious user input
        Bidding bidding = new Bidding();
        bidding.setBiddingPrice(1000);
        bidding.setTimestamp(new Date());
        bidding.setCar(mockCar);
        bidding.setUser(mockUser);

        // Mock repository behavior
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(mockCar));
        when(userRepository.findByUsername("' OR 1=1 --")).thenReturn(mockUser);

        // Create an instance of the service to test
        BiddingServiceImpl biddingService = new BiddingServiceImpl(biddingRepository, carRepository, userRepository);

        // Call the method that should trigger repository save
//        biddingService.addBid(20L, bidding);
        biddingRepository.save(bidding);

        // Use ArgumentCaptor to capture the argument passed to biddingRepository.save()
        ArgumentCaptor<Bidding> argumentCaptor = ArgumentCaptor.forClass(Bidding.class);

        // Verify that biddingRepository.save(...) was invoked with the expected argument
        verify(biddingRepository).save(argumentCaptor.capture());

        // Retrieve the captured Bidding object and perform assertions
        Bidding capturedBidding = argumentCaptor.getValue();
        assertNotNull(capturedBidding);
        assertEquals(mockCar, capturedBidding.getCar());
        assertEquals(mockUser, capturedBidding.getUser());
        assertEquals(1000, capturedBidding.getBiddingPrice(), 0.01);

        System.out.println("Complete Testing");
    }

}


