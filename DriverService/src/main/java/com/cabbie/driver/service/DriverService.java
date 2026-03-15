package com.cabbie.driver.service;

import com.cabbie.driver.dto.DriverRequest;
import com.cabbie.driver.dto.DriverResponse;
import com.cabbie.driver.dto.UserResponse;
import com.cabbie.driver.entity.Driver;
import com.cabbie.driver.enums.DriverStatus;
import com.cabbie.driver.enums.Role;
import com.cabbie.driver.feignClient.UserServiceClient;
import com.cabbie.driver.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    public void registerDriver(String email, DriverRequest driverRequest) {

        UserResponse userResponse = userServiceClient.getUserByEmail(email);
        if(!userResponse.getRole().equals(Role.DRIVER)){
            throw new RuntimeException("User is not a driver");
        }

        Driver driver = new Driver();
        updateDriverFromRequest(driverRequest, driver, userResponse.getId());
        driverRepository.save(driver);
    }

    public DriverResponse getDriverByEmail(String email) {
        UserResponse userResponse = userServiceClient.getUserByEmail(email);
        return mapToDriverResponse(driverRepository.getByUserId(userResponse.getId()));
    }

    public boolean statusUpdate(String status, String email) {
        UserResponse userResponse = userServiceClient.getUserByEmail(email);
        Driver driver =  driverRepository.getByUserId(userResponse.getId());
        if(driver != null){
            driver.setStatus(DriverStatus.valueOf(status));
            return true;
        }
        return false;
    }

    public DriverResponse mapToDriverResponse(Driver d){
        DriverResponse driverResponse = new DriverResponse();
        driverResponse.setUserId(d.getUserId());
        driverResponse.setVehicleModel(d.getVehicleModel());
        driverResponse.setVehicleNumber(d.getVehicleNumber());
        driverResponse.setVehicleType(d.getVehicleType());
        driverResponse.setVehicleCapacity(d.getVehicleCapacity());
        driverResponse.setDriverStatus(DriverStatus.valueOf(d.getStatus().name()));
        driverResponse.setCurrentLatitude(d.getCurrentLatitude());
        driverResponse.setCurrentLongitude(d.getCurrentLongitude());
        driverResponse.setRating(d.getRating());
        driverResponse.setTotalRides(d.getTotalRides());
        driverResponse.setIsActive(d.getIsActive());
        return driverResponse;
    }

    private void updateDriverFromRequest(DriverRequest driverRequest, Driver driver, String id) {

        driver.setUserId(Long.valueOf(id));
        driver.setVehicleModel(driverRequest.getVehicleModel());
        driver.setVehicleNumber(driverRequest.getVehicleNumber());
        driver.setVehicleType(driverRequest.getVehicleType());
        driver.setVehicleCapacity(driverRequest.getVehicleCapacity());
        driver.setStatus(DriverStatus.valueOf(driverRequest.getDriverStatus()));
        driver.setCurrentLatitude(driverRequest.getCurrentLatitude());
        driver.setCurrentLongitude(driverRequest.getCurrentLongitude());
        driver.setRating(driverRequest.getRating());
        driver.setTotalRides(driverRequest.getTotalRides());
        driver.setIsActive(driverRequest.getIsActive());
    }


}
