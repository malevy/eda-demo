package net.malevy.edareservation;

import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
public class SeatsController {

    private final ReservationService reservationService;

    public SeatsController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/seats/reserve")
    public ResponseEntity<?> reserve(final @RequestBody ReserveRequest request) {

        final var reservation = this.reservationService.reserve(request.orderId, request.showId, request.seats);

        return ResponseEntity.ok(reservation);
    }

    @PostMapping("/reservations/{id}/complete")
    public ResponseEntity<?> complete(final @PathVariable("id") String id) {

        try {
            this.reservationService.completeReservation(id);
            final var reservation = this.reservationService.fetch(id);
            return reservation
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        catch (IllegalStateException ise) {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<?> fetchReservation(final @PathVariable("id") String id) {

        final var reservation = this.reservationService.fetch(id);
        return reservation
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Value
    static public class ReserveRequest {
        public String orderId;
        public String showId;
        public List<String> seats;
    }

}

