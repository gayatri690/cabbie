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
    @GeneratedValue
    private Long id;

    private Long userId;

    private String vehicleNumber;

    private String vehicleModel;

    private String vehicleType;

    private Integer vehicleCapacity;

    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    private Double rating;

    private Integer totalRides;

    private Boolean isActive;
}
