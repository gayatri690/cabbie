package com.cabbie.ride.service;

import com.cabbie.ride.dto.CreateRideRequest;
import com.cabbie.ride.dto.RideResponse;
import com.cabbie.ride.entity.Ride;
import com.cabbie.ride.enums.RideStatus;
import com.cabbie.ride.event.RideCreatedEvent;
import com.cabbie.ride.kafka.RideEventProducer;
import com.cabbie.ride.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class RideService {

	@Autowired
	private RideEventProducer rideEventProducer;
	@Autowired
	private final RideRepository rideRepository;
	public static final String EVENT_RIDE_CREATED = "RideCreated";

	@Transactional
	public RideResponse createRide(CreateRideRequest request) {
		Ride ride = Ride.builder()
				.userId(Long.valueOf(request.getUserId()))
				.pickupLatitude(request.getPickupLatitude())
				.pickupLongitude(request.getPickupLongitude())
				.dropoffLatitude(request.getDropoffLatitude())
				.dropoffLongitude(request.getDropoffLongitude())
				.status(RideStatus.REQUESTED)
				.build();
		ride = rideRepository.save(ride);

		RideCreatedEvent event = RideCreatedEvent.builder()
				.eventType(EVENT_RIDE_CREATED)
				.rideId(ride.getId())
				.userId(ride.getUserId())
				.pickupLatitude(ride.getPickupLatitude())
				.pickupLongitude(ride.getPickupLongitude())
				.build();

		TransactionSynchronizationManager.registerSynchronization(
				new TransactionSynchronization() {
					@Override
					public void afterCommit() {
						rideEventProducer.publishRideRequest(event);
					}
				}
		);

		return toResponse(ride);
	}


	private static RideResponse toResponse(Ride ride) {
		return RideResponse.builder()
				.id(ride.getId())
				.userId(ride.getUserId())
				.pickupLatitude(ride.getPickupLatitude())
				.pickupLongitude(ride.getPickupLongitude())
				.dropoffLatitude(ride.getDropoffLatitude())
				.dropoffLongitude(ride.getDropoffLongitude())
				.status(ride.getStatus())
				.driverId(ride.getDriverId())
				.createdAt(ride.getCreatedAt())
				.updatedAt(ride.getUpdatedAt())
				.build();
	}
}
