package net.malevy.edareservation.messages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Publisher {
    private final StreamBridge streamBridge;

    public Publisher(final StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publish(final Envelope<?> envelope, String messageType) {

        final var message = MessageBuilder.withPayload(envelope)
                .setHeader("message-type", messageType)
                .build();

        this.streamBridge.send("reservationPublisher-out-0", message);

    }
}
