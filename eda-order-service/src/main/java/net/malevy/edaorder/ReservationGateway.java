package net.malevy.edaorder;

import net.malevy.edaorder.config.ExternalServicesProperties;
import net.malevy.edaorder.messages.Seats;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class ReservationGateway {

    private final ExternalServicesProperties externalServicesProperties;
    private final RestTemplate restTemplate;

    public ReservationGateway(ExternalServicesProperties externalServicesProperties,
                              RestTemplate restTemplate) {
        this.externalServicesProperties = externalServicesProperties;
        this.restTemplate = restTemplate;
    }

    public Seats reserve(Seats reservationRequest) {

        String reserveUrl = UriComponentsBuilder.fromUri(externalServicesProperties.getReservation())
                .path("/seats/reserve")
                .toUriString();

        var reservationResponse = this.restTemplate
                .postForEntity(reserveUrl,reservationRequest,Seats.class);

        var reservation = reservationResponse.getBody();
        return reservation;
    }

    public void complete(String orderId) {

        String completeUrl = UriComponentsBuilder.fromUri(externalServicesProperties.getReservation())
                .path("/reservations/{id}/complete")
                .build(Map.ofEntries(Map.entry("id", orderId)))
                .toString();

        this.restTemplate.postForEntity(completeUrl, null, Seats.class);

    }

}
