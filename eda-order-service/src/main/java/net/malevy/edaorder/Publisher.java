package net.malevy.edaorder;

import lombok.extern.slf4j.Slf4j;
import net.malevy.edaorder.messages.Envelope;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Publisher {

    public static class MessageTypes {
        public static final String OrderCreated_v1 = "ticketing.order.created.v1";
    }

    private final StreamBridge streamBridge;

    public Publisher(final StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void orderCreated(Order order) {

        final var payload = new Envelope<>(MessageTypes.OrderCreated_v1, order);

        var message = MessageBuilder.withPayload(payload)
                .setHeader("message-type", payload.getMessageType())
                .build();

        this.streamBridge.send("orderPublisher-out-0", message);

        log.info("action: publish | messageId: {} | messageType: {}", payload.getId(), payload.getMessageType());
    }

}
