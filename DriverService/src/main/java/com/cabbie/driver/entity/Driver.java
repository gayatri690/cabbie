package com.cabbie.driver.entity;

import com.cabbie.driver.enums.DriverStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "drivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to User Service User
    //@Column(nullable = false)
    private Long userId;

    // Vehicle details
    private String vehicleModel;

    private String vehicleNumber;

    private String vehicleType;

    private Integer vehicleCapacity;

    // Driver status
    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    // Location (optional for ride matching)
    private Double currentLatitude;

    private Double currentLongitude;

    // Ratings
    private Double rating;

    private Integer totalRides;

    // Account status
    private Boolean isActive;

    // timestamps
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
