package com.cabbie.driver.repository;

import com.cabbie.driver.dto.DriverResponse;
import com.cabbie.driver.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Driver getByUserId(String id);
}
