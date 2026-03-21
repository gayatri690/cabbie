package com.cabbie.ride.dto;

import com.cabbie.ride.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRideRequest  {

	private Long userId;

	private Double pickupLatitude;
	private Double pickupLongitude;

	private Double dropoffLatitude;
	private Double dropoffLongitude;
	private RideStatus status;
}
