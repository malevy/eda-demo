package net.malevy.edareservation.messages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Publisher {
    private Processor processor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public Publisher(final Processor processor) {
        this.processor = processor;
    }

    public void publish(final Envelope<?> envelope, String messageType) {

        final var message = MessageBuilder.withPayload(envelope)
                .setHeader("topic", messageType)
                .build();

        this.processor.output().send(message);

    }
}
