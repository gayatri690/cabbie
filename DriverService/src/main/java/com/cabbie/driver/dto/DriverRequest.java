package com.cabbie.driver.dto;

import lombok.Data;

@Data
public class DriverRequest {

    private Long userId;
    private String vehicleModel;
    private String vehicleNumber;
    private String vehicleType;
    private Integer vehicleCapacity;
    private String driverStatus;
    private Double currentLatitude;
    private Double currentLongitude;
    private Double rating;
    private Integer totalRides;
    private Boolean isActive;

}
