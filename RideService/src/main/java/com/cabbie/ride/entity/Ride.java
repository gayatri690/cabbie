package com.cabbie.ride.entity;

import com.cabbie.ride.enums.RideStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	private Double pickupLatitude;
	private Double pickupLongitude;

	private Double dropoffLatitude;
	private Double dropoffLongitude;

	@Enumerated(EnumType.STRING)
	private RideStatus status;

	private Long driverId;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
