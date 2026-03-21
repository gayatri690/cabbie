package com.cabbie.ride.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Published to Kafka so Driver Service can pick a nearby driver.
 * Keep this schema aligned with Driver Service consumer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideCreatedEvent {

	private String eventType;
	private Long rideId;
	private Long userId;
	private Double pickupLatitude;
	private Double pickupLongitude;
}
