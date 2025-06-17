package net.malevy.edareservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EdaReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdaReservationServiceApplication.class, args);
    }

}
