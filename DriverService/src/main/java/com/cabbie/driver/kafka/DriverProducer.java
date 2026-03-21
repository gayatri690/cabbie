package com.cabbie.driver.kafka;

import com.cabbie.driver.dto.DriverAssignedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DriverProducer {

    @Autowired
    private KafkaTemplate<String, DriverAssignedEvent> kafkaTemplate;

    private static final String TOPIC = "driver-assigned-topic";

    public void publishDriverAssigned(DriverAssignedEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
