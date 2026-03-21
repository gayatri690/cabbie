package com.cabbie.ride.controller;

import com.cabbie.ride.dto.CreateRideRequest;
import com.cabbie.ride.dto.RideResponse;
import com.cabbie.ride.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RideController {

	@Autowired
	private RideService rideService;

	@PostMapping("/create-ride")
	public ResponseEntity<RideResponse> createRide(@Valid @RequestBody CreateRideRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(rideService.createRide(request));
	}
}
