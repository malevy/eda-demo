package net.malevy.edareservation;

import lombok.extern.slf4j.Slf4j;
import net.malevy.edareservation.messages.*;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WorkflowProcessor {

    private final Publisher publisher;
    private final ReservationService reservationService;

    public static class MessageTypes {
        public static final String SEATSRESERVED_V1 = "ticketing.seats.reserved.v1";
        public static final String SEATSASSIGNED_V1 = "ticketing.seats.assigned.v1";
        public static final String ORDERCREATED_V1 = "ticketing.order.created.v1";
        public static final String PAYMENTAPPROVED_V1 = "ticketing.payment.accepted.v1";

    }

    public WorkflowProcessor(final Publisher publisher,
                             final ReservationService reservationService) {

        this.publisher = publisher;
        this.reservationService = reservationService;
    }

    @Bean
    public java.util.function.Consumer<Message<Object>> reservationProcessor() {
        return message -> {
            String messageType = (String) message.getHeaders().get("message-type");
            if ("ticketing.order.created.v1".equals(messageType)) {
                orderCreatedHandler((Envelope<Order>) message.getPayload());
            } else if ("ticketing.payment.accepted.v1".equals(messageType)) {
                paymentApprovedHandler((Envelope<PaymentApproved>) message.getPayload());
            }
        };
    }

    private void orderCreatedHandler(final Envelope<Order> envelope) {
        log.info("action: received | messageId: {} | messageType: {} | orderId: {}",
                envelope.getId(), envelope.getMessageType(), envelope.getPayload().getId());

        final var order = envelope.getPayload();

        final var reservation = this.reservationService.reserve(
                order.getId(),
                order.getShowId(),
                order.getSeats());

        final Envelope<Seats> seatsReservedEvent = new Envelope<>(MessageTypes.SEATSRESERVED_V1, reservation);

        this.publisher.publish(seatsReservedEvent, MessageTypes.SEATSRESERVED_V1);
        log.info("action: publishing | messageId: {} | messageType: {} | orderId: {}",
            seatsReservedEvent.getId(), seatsReservedEvent.getMessageType(), order.getId());
    }

    private void paymentApprovedHandler(final Envelope<PaymentApproved> envelope) {
        log.info("action: received | messageId: {} | messageType: {} | orderId: {}",
                envelope.getId(), envelope.getMessageType(), envelope.getPayload().getOrderId());

        final var payment = envelope.getPayload();

        this.reservationService.completeReservation(payment.getOrderId());
        this.reservationService.fetch(payment.getOrderId()).ifPresent(seats -> {
            final Envelope<Seats> seatsReservedEvent = new Envelope<>(MessageTypes.SEATSASSIGNED_V1, seats);

            this.publisher.publish(seatsReservedEvent, MessageTypes.SEATSASSIGNED_V1);
            log.info("action: publishing | messageId: {} | messageType: {} | orderId: {}",
                    seatsReservedEvent.getId(), seatsReservedEvent.getMessageType(), seats.getOrderId());
        });

    }

}
