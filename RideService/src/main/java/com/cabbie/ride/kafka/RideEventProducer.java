package com.cabbie.ride.kafka;

import com.cabbie.ride.event.RideCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideEventProducer {

	@Autowired
	private KafkaTemplate<Long, RideCreatedEvent> kafkaTemplate;

	private static final String TOPIC = "ride-request-topic";

	public void publishRideRequest(RideCreatedEvent event) {
		kafkaTemplate.send(TOPIC, event.getRideId(), event);
	}
}
