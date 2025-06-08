package net.malevy.edapaymentservice;

import lombok.extern.slf4j.Slf4j;
import net.malevy.edapaymentservice.messages.Envelope;
import net.malevy.edapaymentservice.messages.PaymentApproved;
import net.malevy.edapaymentservice.messages.Seats;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WorkflowProcessor {

    private final Publisher publisher;

    public static class MessageTypes {
        public static final String PAYMENTACCEPTED_V1 = "ticketing.payment.accepted.v1";
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public WorkflowProcessor(final Publisher publisher) {

        this.publisher = publisher;
    }

    @Bean
    public java.util.function.Consumer<Message<Envelope<Seats>>> paymentProcessor() {
        return message -> {
            String messageType = (String) message.getHeaders().get("message-type");
            if ("ticketing.seats.reserved.v1".equals(messageType)) {
                seatsReservedHandler(message.getPayload());
            }
        };
    }

    private void seatsReservedHandler(final Envelope<Seats> envelope) {
        log.info("action: received | messageId: {} | messageType: {} | orderId: {}",
                envelope.getId(), envelope.getMessageType(), envelope.getPayload().getOrderId());

        final var seats = envelope.getPayload();

        // process payment here...
        log.info("action: payment-processed | orderId: {}", seats.getOrderId());

        final var paymentApprovedPayload = new PaymentApproved(seats.getOrderId(),
                                                seats.getShowId(),
                                                RandomStringUtils.random(12, true, true),
                                                "Your payment was accepted");

        final var paymentApprovedEvent = new Envelope<>(MessageTypes.PAYMENTACCEPTED_V1, paymentApprovedPayload);

        this.publisher.publish(paymentApprovedEvent, MessageTypes.PAYMENTACCEPTED_V1);
        log.info("action: publishing | messageId: {} | messageType: {} | orderId: {}",
                paymentApprovedEvent.getId(), paymentApprovedEvent.getMessageType(), seats.getOrderId());
    }

}
