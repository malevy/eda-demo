package net.malevy.edareservation;

import lombok.extern.slf4j.Slf4j;
import net.malevy.edareservation.messages.*;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
public class WorkflowProcessor {

    private Publisher publisher;
    private final ReservationService reservationService;

    public static class MessageTypes {
        public static final String SEATSRESERVED_V1 = "ticketing.seats.reserved.v1";
        public static final String SEATSASSIGNED_V1 = "ticketing.seats.assigned.v1";
        public static final String ORDERCREATED_V1 = "ticketing.order.created.v1";
        public static final String PAYMENTAPPROVED_V1 = "ticketing.payment.accepted.v1";

    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public WorkflowProcessor(final Publisher publisher,
                             final ReservationService reservationService) {

        this.publisher = publisher;
        this.reservationService = reservationService;
    }

    @StreamListener(target = Processor.INPUT, condition = "headers['topic']=='ticketing.order.created.v1'")
    public void orderCreatedHandler(final @Payload Envelope<Order> envelope) {
        log.info("action: received | messageId: {} | messageType: {} | orderId: {}",
                envelope.getId(), envelope.getMessageType(), envelope.getPayload().getId());

        final var order = envelope.getPayload();

        final Seats reservation = this.reservationService.reserve(
                order.getId(),
                order.getShowId(),
                order.getSeats());

        final Envelope<Seats> seatsReservedEvent = new Envelope<>(MessageTypes.SEATSRESERVED_V1, reservation);

        this.publisher.publish(seatsReservedEvent, MessageTypes.SEATSRESERVED_V1);
        log.info("action: publishing | messageId: {} | messageType: {} | orderId: {}",
            seatsReservedEvent.getId(), seatsReservedEvent.getMessageType(), order.getId());

    }

    @StreamListener(target = Processor.INPUT, condition = "headers['topic']=='ticketing.payment.accepted.v1'")
    public void paymentApprovedHandler(final @Payload Envelope<PaymentApproved> envelope) {
        log.info("action: received | messageId: {} | messageType: {} | orderId: {}",
                envelope.getId(), envelope.getMessageType(), envelope.getPayload().getOrderId());

        final var payment = envelope.getPayload();

        this.reservationService.completeReservation(payment.getOrderId());
        final var seats = this.reservationService.fetch(payment.getOrderId()).get();

        final Envelope<Seats> seatsReservedEvent = new Envelope<>(MessageTypes.SEATSASSIGNED_V1, seats);

        this.publisher.publish(seatsReservedEvent, MessageTypes.SEATSASSIGNED_V1);
        log.info("action: publishing | messageId: {} | messageType: {} | orderId: {}",
                seatsReservedEvent.getId(), seatsReservedEvent.getMessageType(), seats.getOrderId());

    }

}
