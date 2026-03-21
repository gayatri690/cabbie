package com.cabbie.driver.kafka;

import com.cabbie.driver.dto.DriverAssignedEvent;
import com.cabbie.driver.dto.NearbyDriverResponse;
import com.cabbie.driver.dto.RideRequestedEvent;
import com.cabbie.driver.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideRequestConsumer {

    private static final double DEFAULT_SEARCH_RADIUS_KM = 5.0;

    @Autowired
    private DriverService driverService;

    @Autowired
    private DriverProducer driverProducer;

    @KafkaListener(topics = "ride-request-topic", groupId = "driver-group")
    public void consumeRideRequest(RideRequestedEvent event) {

        // 1. Find nearby drivers
        List<NearbyDriverResponse> drivers =
                driverService.findNearbyDrivers(
                        event.getPickupLatitude(),
                        event.getPickupLongitude(),
                        DEFAULT_SEARCH_RADIUS_KM
                );

        // 2. Pick nearest driver
        if (!drivers.isEmpty()) {
            NearbyDriverResponse selectedDriver = drivers.get(0);

            // 3. Publish driver assigned event
            DriverAssignedEvent assignedEvent = new DriverAssignedEvent();
            assignedEvent.setRideId(event.getRideId());
            assignedEvent.setDriverId(selectedDriver.getDriverId());

            driverProducer.publishDriverAssigned(assignedEvent);
        }
    }
}
