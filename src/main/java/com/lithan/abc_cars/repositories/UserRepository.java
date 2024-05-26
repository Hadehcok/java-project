package com.lithan.abc_cars.repositories;

import com.lithan.abc_cars.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    UserEntity findFirstByUsername(String username);
    UserEntity findByEmail(String email);
;
}
