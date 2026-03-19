package com.cabbie.driver.service;

import com.cabbie.driver.dto.*;
import com.cabbie.driver.entity.Driver;
import com.cabbie.driver.exception.UserServiceUnavailableException;
import com.cabbie.driver.entity.DriverLocation;
import com.cabbie.driver.enums.DriverStatus;
import com.cabbie.driver.enums.Role;
import com.cabbie.driver.feignClient.UserServiceClient;
import com.cabbie.driver.repository.DriverLocationRepository;
import com.cabbie.driver.repository.DriverRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private DriverLocationRepository driverLocationRepository;

    @Autowired
    @Lazy
    private DriverService self;

    public void registerDriver(String email, DriverRequest driverRequest) {

        UserResponse userResponse = self.getUserByEmailWithResilience(email);
        if (userResponse.getId() == null) {
            throw new UserServiceUnavailableException("User service temporarily unavailable");
        }
        if(!userResponse.getRole().equals(Role.DRIVER)){
            throw new RuntimeException("User is not a driver");
        }
        Driver driver = new Driver();
        updateDriverFromRequest(driverRequest, driver, userResponse.getId());
        driverRepository.save(driver);
    }

    public DriverResponse getDriverByEmail(String email) {
        UserResponse userResponse = self.getUserByEmailWithResilience(email);
        if (userResponse.getId() == null) {
            throw new UserServiceUnavailableException("User service temporarily unavailable");
        }
        Driver driver = driverRepository.getByUserId(userResponse.getId());
        if(driver == null) throw new RuntimeException("Driver ID not found");
        return mapToDriverResponse(driver);
    }

    public boolean statusUpdate(String status, String email) {
        UserResponse userResponse = self.getUserByEmailWithResilience(email);
        if (userResponse.getId() == null) {
            throw new UserServiceUnavailableException("User service temporarily unavailable");
        }
        Driver driver =  driverRepository.getByUserId(userResponse.getId());
        if(driver != null){
            driver.setStatus(DriverStatus.valueOf(status.toUpperCase()));
            driverRepository.save(driver);
            return true;
        }
        return false;
    }

    public List<NearbyDriverResponse> findNearbyDrivers(double userLat, double userLng, double radiusKm) {

        // 1. Get all available drivers
        List<Driver> drivers =
                driverRepository.findByStatusAndIsActive(DriverStatus.AVAILABLE, true);

        List<NearbyDriverResponse> nearbyDrivers = new ArrayList<>();

        for (Driver driver : drivers) {

            // 2. Get driver location
            DriverLocation location =
                    driverLocationRepository.findById(driver.getUserId()).orElse(null);

            if (location == null) continue;

            // 3. Calculate distance
            double distance = calculateDistance(
                    userLat,
                    userLng,
                    location.getLatitude(),
                    location.getLongitude()
            );

            // 4. Filter drivers within radius
            if (distance <= radiusKm) {

                // 5. Get driver info from User Service
                UserResponse user =
                        userServiceClient.getUserById(driver.getUserId());

                NearbyDriverResponse response =
                        NearbyDriverResponse.builder()
                                .driverId(driver.getId())
                                .latitude(location.getLatitude())
                                .longitude(location.getLongitude())
                                .driverName(user.getFirstName() + " " + user.getLastName())
                                .driverPhoneNumber(user.getPhone())
                                .distance(distance)
                                .build();

                nearbyDrivers.add(response);
            }
        }

        // 6. Sort drivers by distance
        nearbyDrivers.sort(
                Comparator.comparingDouble(NearbyDriverResponse::getDistance)
        );

        return nearbyDrivers;
    }

    private double calculateDistance(double lat1, double lon1,
                                     double lat2, double lon2) {

        final int EARTH_RADIUS = 6371; // km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    public void updateLocation(String email, DriverLocationRequest request) {
        UserResponse userResponse = userServiceClient.getUserByEmail(email);
        Driver driver =  driverRepository.getByUserId(userResponse.getId());
        if(driver == null) throw new RuntimeException("Driver ID not found");
        DriverLocation driverLocation =
                driverLocationRepository.findById(driver.getId())
                        .orElse(new DriverLocation());
        driverLocation.setDriverId(driver.getId());
        driverLocation.setDriverId(driver.getUserId());
        driverLocation.setLatitude(request.getLatitude());
        driverLocation.setLongitude(request.getLongitude());
        driverLocation.setLastUpdated(LocalDateTime.now());
        driverLocationRepository.save(driverLocation);
    }

    public DriverResponse mapToDriverResponse(Driver d){
        DriverResponse driverResponse = new DriverResponse();
        driverResponse.setUserId(d.getUserId());
        driverResponse.setVehicleModel(d.getVehicleModel());
        driverResponse.setVehicleNumber(d.getVehicleNumber());
        driverResponse.setVehicleType(d.getVehicleType());
        driverResponse.setVehicleCapacity(d.getVehicleCapacity());
        driverResponse.setDriverStatus(DriverStatus.valueOf(d.getStatus().name()));
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
        driver.setRating(driverRequest.getRating());
        driver.setTotalRides(driverRequest.getTotalRides());
        driver.setIsActive(driverRequest.getIsActive());
    }

    @Retry(name = "userService")
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackUser")
    public UserResponse getUserByEmailWithResilience(String email) {
        System.out.println("Calling user service...");
        return userServiceClient.getUserByEmail(email);
    }
    public UserResponse fallbackUser(String email, Throwable t) {
        System.out.println("Fallback triggered: " + t.getMessage());

        UserResponse user = new UserResponse();
        user.setEmail(email);
        user.setFirstName("Unavailable");

        return user;
    }

}
