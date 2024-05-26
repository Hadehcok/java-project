package com.lithan.abc_cars.services;

import com.lithan.abc_cars.dtos.BiddingDto;
import com.lithan.abc_cars.models.Bidding;

import java.util.List;

public interface BiddingService {
    void addBid(Long carId, BiddingDto biddingDto);
    List<Bidding> getBidsByCarId(Long carId);
    List<Bidding> getBidsByUserId(Long userId);

    List<Bidding> getAllBiddings();
    public void acceptBidding(Long biddingId);
}
