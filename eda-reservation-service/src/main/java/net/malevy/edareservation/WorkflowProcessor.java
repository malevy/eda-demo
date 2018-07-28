package net.malevy.edareservation;

import lombok.extern.slf4j.Slf4j;
import net.malevy.edareservation.messages.Envelope;
import net.malevy.edareservation.messages.Order;
import net.malevy.edareservation.messages.Publisher;
import net.malevy.edareservation.messages.Seats;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
public class WorkflowProcessor {

    private Publisher publisher;

    public static class MessageTypes {
        public static final String ORDERCREATED_V1 = "ticketing.order.created.v1";
        public static final String SEATSRESERVED_V1 = "ticketing.seats.reserved.v1";
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public WorkflowProcessor(final Publisher publisher) {

        this.publisher = publisher;
    }

    @StreamListener(target = Processor.INPUT, condition = "headers['topic']=='ticketing.order.created.v1'")
    public void orderCreatedHandler(@Payload Envelope<Order> envelope) {
        log.info("action: received | messageId: {} | messageType: {} | orderId: {}",
                envelope.getId(), envelope.getMessageType(), envelope.getPayload().getId());

        // do something useful here...

        final Order order = envelope.getPayload();
        final Seats seatsReservedPayload = new Seats(order.getId(),
                                                order.getShowId(),
                                                Seats.Statuses.RESERVED,
                                                new ArrayList<>(order.getSeats()));

        final Envelope<Seats> seatsReservedEvent = new Envelope<>(MessageTypes.SEATSRESERVED_V1, seatsReservedPayload);

        this.publisher.publish(seatsReservedEvent, MessageTypes.SEATSRESERVED_V1);
        log.info("action: publishing | messageId: {} | messageType: {} | orderId: {}",
            seatsReservedEvent.getId(), seatsReservedEvent.getMessageType(), order.getId());

    }

}
