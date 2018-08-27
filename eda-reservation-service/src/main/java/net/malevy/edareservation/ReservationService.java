package net.malevy.edareservation;

import lombok.extern.slf4j.Slf4j;
import net.malevy.edareservation.messages.Seats;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReservationService {


    private final ReservationRepository reservationRepository;

    public ReservationService(final ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Seats reserve(final String orderId,
                  final String showId,
                  final List<String> seats) {

        final var reservation = new Seats(orderId,
                showId,
                Seats.Statuses.RESERVED,
                new ArrayList<>(seats));

        this.reservationRepository.save(reservation);
        log.info("action: seats-reserved | orderId: {}", orderId);

        return reservation;

    }

    public void completeReservation(String id) {

        final var reservation = this.fetch(id);

        reservation.ifPresentOrElse(
                r -> {
                    r.setStatus(Seats.Statuses.SOLD);
                    this.reservationRepository.save(r);
                    log.info("action: seats-assigned | orderId: {}", r.getOrderId());
                },
                () -> {throw new IllegalStateException("unknown reservation");}
        );

    }

    public Optional<Seats> fetch(String id) {

        log.debug("action: fetching-reservation | orderId: {}", id);
        return this.reservationRepository.Retrieve(id);

    }
}
