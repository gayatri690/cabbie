package com.cabbie.driver.service;

import com.cabbie.driver.dto.DriverRequest;
import com.cabbie.driver.dto.UserResponse;
import com.cabbie.driver.entity.Driver;
import com.cabbie.driver.enums.DriverStatus;
import com.cabbie.driver.enums.Role;
import com.cabbie.driver.feignClient.UserServiceClient;
import com.cabbie.driver.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    public void registerDriver(String email, DriverRequest driverRequest) {

        UserResponse userResponse = userServiceClient.getUserByEmail(email);
        if(userResponse.getRole() != Role.DRIVER){
            throw new RuntimeException("User is not a driver");
        }

        Driver driver = new Driver();
        updateDriverFromRequest(driverRequest, driver);
        driverRepository.save(driver);
    }

    private void updateDriverFromRequest(DriverRequest driverRequest, Driver driver) {

        if (driverRequest.getUserId() != null) {
            driver.setUserId(driverRequest.getUserId());
        }

        if (driverRequest.getVehicleModel() != null) {
            driver.setVehicleModel(driverRequest.getVehicleModel());
        }

        if (driverRequest.getVehicleNumber() != null) {
            driver.setVehicleNumber(driverRequest.getVehicleNumber());
        }

        if (driverRequest.getVehicleType() != null) {
            driver.setVehicleType(driverRequest.getVehicleType());
        }

        if (driverRequest.getVehicleCapacity() != null) {
            driver.setVehicleCapacity(driverRequest.getVehicleCapacity());
        }

        if (driverRequest.getDriverStatus() != null) {
            driver.setStatus(DriverStatus.valueOf(driverRequest.getDriverStatus()));
        }

        if (driverRequest.getCurrentLatitude() != null) {
            driver.setCurrentLatitude(driverRequest.getCurrentLatitude());
        }

        if (driverRequest.getCurrentLongitude() != null) {
            driver.setCurrentLongitude(driverRequest.getCurrentLongitude());
        }

        if (driverRequest.getRating() != null) {
            driver.setRating(driverRequest.getRating());
        }

        if (driverRequest.getTotalRides() != null) {
            driver.setTotalRides(driverRequest.getTotalRides());
        }

        if (driverRequest.getIsActive() != null) {
            driver.setIsActive(driverRequest.getIsActive());
        }
    }
}
