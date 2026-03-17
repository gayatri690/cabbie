package com.cabbie.driver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NearbyDriverResponse {
    private Long driverId;
    private double latitude;
    private double longitude;
    private String driverName;
    private String driverPhoneNumber;
    private double distance;
}
