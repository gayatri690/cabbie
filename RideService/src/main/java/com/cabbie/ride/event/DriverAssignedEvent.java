package com.cabbie.ride.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Consumed from Driver Service after a driver is assigned.
 * Schema must match what Driver Service publishes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverAssignedEvent {

	private Long rideId;
	private Long driverId;
}
