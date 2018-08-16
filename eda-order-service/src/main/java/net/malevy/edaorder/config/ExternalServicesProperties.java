package net.malevy.edaorder.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
            @Value("${app.services.reservation.port}") String reservationPort,
            @Value("${app.services.payment.host}") String paymentHost,
            @Value("${app.services.payment.port}") String paymentPort) {

        payment = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(paymentHost)
                .port(paymentPort)
                .build().toUri();
        log.info("payment-service: {}", payment.toString());

        reservation = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(reservationHost)
                .port(reservationPort)
                .build().toUri();
        log.info("reservation-service: {}", reservation.toString());
    }
}
