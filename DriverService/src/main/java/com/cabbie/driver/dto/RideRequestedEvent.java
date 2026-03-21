package com.cabbie.driver.dto;

import lombok.Data;

/**
 * Must match JSON from RideService {@code RideCreatedEvent} on topic {@code ride-request-topic}.
 */
@Data
public class RideRequestedEvent {

	private String eventType;
	private Long rideId;
	private Long userId;
	private Double pickupLatitude;
	private Double pickupLongitude;
}
