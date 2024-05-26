package com.lithan.abc_cars.repositories;

import com.lithan.abc_cars.models.Bidding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BiddingRepository extends JpaRepository<Bidding, Long> {
    List<Bidding> findByCarId(Long carId);
    List<Bidding> findByUserId(Long userId);
}
