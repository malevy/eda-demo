package net.malevy.edareservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;

@SpringBootApplication
@EnableCaching
@EnableBinding(Processor.class)
@EnableDiscoveryClient
public class EdaReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdaReservationServiceApplication.class, args);
    }

}
