package net.malevy.edareservation;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController()
public class SeatsController {

    private final ReservationService reservationService;

    public SeatsController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/seats/reserve")
    public Mono<ResponseEntity<?>> reserve(final @Payload String orderId,
                                           final @Payload String showId,
                                           final @Payload List<String> seats) {

        final var reservation = this.reservationService.reserve(orderId, showId, seats);

        return Mono.just(ResponseEntity.ok(reservation));
    }

    @PostMapping("reservations/{id}/complete")
    public Mono<ResponseEntity<?>> complete(final @PathVariable String id) {

        try {
            this.reservationService.completeReservation(id);
            return Mono.just(ResponseEntity.ok(this.fetchReservation(id)));
        }
        catch (IllegalStateException ise) {
            return Mono.just(ResponseEntity.notFound().build());
        }

    }

    @GetMapping("/reservations/{id}")
    public Mono<ResponseEntity<?>> fetchReservation(final @PathVariable String id) {

        final var reservation = this.reservationService.fetch(id);
        final var response = reservation
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

        return Mono.just(response);
    }

}
