package com.lithan.abc_cars.repositories;

import com.lithan.abc_cars.models.Cars;
import com.lithan.abc_cars.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Cars, Long> {
    List<Cars> findByStatus(String status);
    List<Cars> findByCreatedBy(UserEntity createdBy);
    @Query("SELECT c FROM Cars c WHERE c.name LIKE CONCAT('%', :query, '%') ")
    List<Cars> searchCarsName(String query);
    @Query("SELECT c FROM Cars c WHERE c.model LIKE CONCAT('%', :query, '%') ")
    List<Cars> searchCarsModel(String query);

    List<Cars> findByYear(int year);
    List<Cars> findByPriceBetween(int minPrice, int maxPrice);
}
