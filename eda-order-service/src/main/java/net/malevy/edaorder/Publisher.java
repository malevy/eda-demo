package net.malevy.edaorder;

import lombok.extern.slf4j.Slf4j;
import net.malevy.edaorder.messages.Envelope;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Publisher {

    public static class MessageTypes {
        public static final String OrderCreated_v1 = "ticketing.order.created.v1";
    }

    private Source source;

    public Publisher(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") final Source source) {
        this.source = source;
    }

    public void orderCreated(Order order) {

        final var payload = new Envelope<>(MessageTypes.OrderCreated_v1, order);

        var message = MessageBuilder.withPayload(payload)
                // spring cloud stream is configure to use the value of the topic header
                // as the topic when publishing messages on the output channel
                .setHeader("topic", payload.getMessageType())
                .build();

        this.source.output().send(message);

        log.info("action: publish | messageId: {} | messageType: {}", payload.getId(), payload.getMessageType());
    }

}
