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
    private OrderRepository orderRepository;

    public static class MessageTypes {
        public static final String SEATSRESERVED_V1 = "ticketing.seats.reserved.v1";
        public static final String SEATSASSIGNED_V1 = "ticketing.seats.assigned.v1";
        public static final String ORDERCREATED_V1 = "ticketing.order.created.v1";
        public static final String PAYMENTAPPROVED_V1 = "ticketing.payment.accepted.v1";

    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public WorkflowProcessor(final Publisher publisher,
                             final OrderRepository orderRepository) {

        this.publisher = publisher;
        this.orderRepository = orderRepository;
    }

    @StreamListener(target = Processor.INPUT, condition = "headers['topic']=='ticketing.order.created.v1'")
    public void orderCreatedHandler(final @Payload Envelope<Order> envelope) {
        log.info("action: received | messageId: {} | messageType: {} | orderId: {}",
                envelope.getId(), envelope.getMessageType(), envelope.getPayload().getId());

        final var order = envelope.getPayload();

        // do something useful here...
        this.orderRepository.Register(order);
        log.info("action: seats-reserved | orderId: {}", order.getId());

        final Seats seatsReservedPayload = new Seats(order.getId(),
                                                order.getShowId(),
                                                Seats.Statuses.RESERVED,
                                                new ArrayList<>(order.getSeats()));

        final Envelope<Seats> seatsReservedEvent = new Envelope<>(MessageTypes.SEATSRESERVED_V1, seatsReservedPayload);

        this.publisher.publish(seatsReservedEvent, MessageTypes.SEATSRESERVED_V1);
        log.info("action: publishing | messageId: {} | messageType: {} | orderId: {}",
            seatsReservedEvent.getId(), seatsReservedEvent.getMessageType(), order.getId());

    }

    @StreamListener(target = Processor.INPUT, condition = "headers['topic']=='ticketing.payment.accepted.v1'")
    public void paymentApprovedHandler(final @Payload Envelope<PaymentApproved> envelope) {
        log.info("action: received | messageId: {} | messageType: {} | orderId: {}",
                envelope.getId(), envelope.getMessageType(), envelope.getPayload().getOrderId());

        final var payment = envelope.getPayload();
        final var order = this.orderRepository.Retrieve(payment.getOrderId()).get();

        // business stuff goes here
        log.info("action: seats-assigned | orderId: {}", order.getId());

        final Seats seatsReservedPayload = new Seats(order.getId(),
                order.getShowId(),
                Seats.Statuses.RESERVED,
                new ArrayList<>(order.getSeats()));

        final Envelope<Seats> seatsReservedEvent = new Envelope<>(MessageTypes.SEATSASSIGNED_V1, seatsReservedPayload);

        this.publisher.publish(seatsReservedEvent, MessageTypes.SEATSASSIGNED_V1);
        log.info("action: publishing | messageId: {} | messageType: {} | orderId: {}",
                seatsReservedEvent.getId(), seatsReservedEvent.getMessageType(), order.getId());

    }

}
