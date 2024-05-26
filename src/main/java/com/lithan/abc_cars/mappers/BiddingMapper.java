package com.lithan.abc_cars.mappers;


import com.lithan.abc_cars.dtos.BiddingDto;
import com.lithan.abc_cars.models.Bidding;

public class BiddingMapper {
    public static Bidding mapToBidding(BiddingDto biddingDto){
        return Bidding.builder()
                .id(biddingDto.getId())
                .biddingPrice(biddingDto.getBiddingPrice())
                .car(biddingDto.getCarId())
                .user(biddingDto.getUserId())
                .accepted(biddingDto.isAccepted())
                .build();
    }


}
