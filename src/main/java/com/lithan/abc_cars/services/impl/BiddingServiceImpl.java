package com.lithan.abc_cars.services.impl;

import com.lithan.abc_cars.dtos.BiddingDto;
import com.lithan.abc_cars.mappers.BiddingMapper;
import com.lithan.abc_cars.models.Bidding;
import com.lithan.abc_cars.models.Cars;
import com.lithan.abc_cars.models.UserEntity;
import com.lithan.abc_cars.repositories.BiddingRepository;
import com.lithan.abc_cars.repositories.CarRepository;
import com.lithan.abc_cars.repositories.UserRepository;
import com.lithan.abc_cars.security.SecurityUtil;
import com.lithan.abc_cars.services.BiddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BiddingServiceImpl implements BiddingService {
    private final BiddingRepository biddingRepository;
    private CarRepository carRepository;
    private UserRepository userRepository;

    @Autowired
    public BiddingServiceImpl(BiddingRepository biddingRepository, CarRepository carRepository, UserRepository userRepository) {
        this.biddingRepository = biddingRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addBid(Long carId, BiddingDto biddingDto) {
        Cars car = carRepository.findById(carId).get();
        Bidding bidding = BiddingMapper.mapToBidding(biddingDto);
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);

        bidding.setTimestamp(new Date());
        bidding.setCar(car);
        bidding.setUser(user);
        biddingRepository.save(bidding);
    }

    @Override
    public List<Bidding> getBidsByCarId(Long carId) {
        return biddingRepository.findByCarId(carId);
    }

    @Override
    public List<Bidding> getBidsByUserId(Long userId) {
        return biddingRepository.findByUserId(userId);
    }

    @Override
    public List<Bidding> getAllBiddings() {
        return biddingRepository.findAll();
    }

    @Override
    public void acceptBidding(Long biddingId) {
        Optional<Bidding> biddingOptional = biddingRepository.findById(biddingId);
        biddingOptional.ifPresent(bidding -> {
            // Update the accepted status to true
            bidding.setAccepted(true);
            // Save the updated bidding
            biddingRepository.save(bidding);
        });
    }
}
