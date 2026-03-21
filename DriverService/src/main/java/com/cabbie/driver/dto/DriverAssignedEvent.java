package com.cabbie.driver.dto;

import lombok.Data;

/**
 * Published to {@code driver-assigned-topic}. JSON shape must match RideService {@code DriverAssignedEvent}.
 */
@Data
public class DriverAssignedEvent {

    private Long rideId;
    private Long driverId;
}
