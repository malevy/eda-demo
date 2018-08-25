package net.malevy.edaorder.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Getter
@Component
@Slf4j
public class ExternalServicesProperties {

    private URI payment;
    private URI reservation;

    @Autowired
    public ExternalServicesProperties(
            @Value("${app.services.reservation.host}") String reservationHost,
            @Value("${app.services.payment.host}") String paymentHost) {

        payment = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(paymentHost)
                .build().toUri();
        log.info("payment-service: {}", payment.toString());

        reservation = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(reservationHost)
                .build().toUri();
        log.info("reservation-service: {}", reservation.toString());
    }
}
