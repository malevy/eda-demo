package net.malevy.edapaymentservice;

import lombok.extern.slf4j.Slf4j;
import net.malevy.edapaymentservice.messages.Envelope;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Publisher {
    private final Processor processor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public Publisher(final Processor processor) {
        this.processor = processor;
    }

    public void publish(final Envelope<?> envelope, String messageType) {

        final var message = MessageBuilder.withPayload(envelope)
                .setHeader("message-type", messageType)
                .build();

        this.processor.output().send(message);

    }
}
