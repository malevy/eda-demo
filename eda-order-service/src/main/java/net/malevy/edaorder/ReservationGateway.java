package net.malevy.edaorder;

import lombok.extern.slf4j.Slf4j;
import net.malevy.edaorder.config.ExternalServicesProperties;
import net.malevy.edaorder.messages.Seats;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@Slf4j
public class ReservationGateway {

    private final ExternalServicesProperties externalServicesProperties;
    private final RestTemplate restTemplate;

    public ReservationGateway(ExternalServicesProperties externalServicesProperties,
                              RestTemplate restTemplate) {
        this.externalServicesProperties = externalServicesProperties;
        this.restTemplate = restTemplate;
    }

    public Seats reserve(Seats reservationRequest) {

        final var reserveUrl = UriComponentsBuilder.fromUri(externalServicesProperties.getReservation())
                .path("/seats/reserve")
                .toUriString();

        log.info("reserving seats at {}", reserveUrl);

        final var reservationResponse = this.restTemplate
                .postForEntity(reserveUrl,reservationRequest,Seats.class);

        return reservationResponse.getBody();
    }

    public void complete(String orderId) {

        final var completeUrl = UriComponentsBuilder.fromUri(externalServicesProperties.getReservation())
                .path("/reservations/{id}/complete")
                .build(Map.ofEntries(Map.entry("id", orderId)))
                .toString();

        log.info("assigning seats at {}", completeUrl);

        this.restTemplate.postForEntity(completeUrl, null, Seats.class);

    }

}
