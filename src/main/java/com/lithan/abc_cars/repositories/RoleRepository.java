package com.lithan.abc_cars.repositories;

import com.lithan.abc_cars.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

}
