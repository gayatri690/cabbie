package com.cabbie.driver.dto;

import com.cabbie.driver.enums.DriverStatus;
import lombok.Data;

@Data
public class DriverResponse {

    private Long userId;
    private String vehicleModel;
    private String vehicleNumber;
    private String vehicleType;
    private Integer vehicleCapacity;
    private DriverStatus driverStatus;
    private Double currentLatitude;
    private Double currentLongitude;
    private Double rating;
    private Integer totalRides;
    private Boolean isActive;
}
