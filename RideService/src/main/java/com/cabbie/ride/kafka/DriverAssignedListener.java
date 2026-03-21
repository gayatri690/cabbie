package com.cabbie.ride.kafka;

import com.cabbie.ride.entity.Ride;
import com.cabbie.ride.enums.RideStatus;
import com.cabbie.ride.event.DriverAssignedEvent;
import com.cabbie.ride.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriverAssignedListener {

	@Autowired
	private RideRepository rideRepository;

	@KafkaListener(topics = "driver-assigned-topic", groupId = "ride-group")
	public void consume(DriverAssignedEvent event) {

		Ride ride = rideRepository.findById(event.getRideId()).orElse(null);

		if (ride == null) return;

		ride.setDriverId(event.getDriverId());
		ride.setStatus(RideStatus.DRIVER_ASSIGNED);

		rideRepository.save(ride);
	}
}
