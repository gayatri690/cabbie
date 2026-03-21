package com.cabbie.ride.dto;

import com.cabbie.ride.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideResponse {

	private Long id;
	private Long userId;
	private Double pickupLatitude;
	private Double pickupLongitude;
	private Double dropoffLatitude;
	private Double dropoffLongitude;
	private RideStatus status;
	private Long driverId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
