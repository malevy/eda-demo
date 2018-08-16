package net.malevy.edareservation;

import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController()
public class SeatsController {

    private final ReservationService reservationService;

    public SeatsController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/seats/reserve")
    public Mono<ResponseEntity<?>> reserve(final @RequestBody ReserveRequest request) {

        final var reservation = this.reservationService.reserve(request.orderId, request.showId, request.seats);

        return Mono.just(ResponseEntity.ok(reservation));
    }

    @PostMapping("/reservations/{id}/complete")
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

@Value
class ReserveRequest {
    public String orderId;
    public String showId;
    public List<String> seats;
}
