package net.malevy.edaorder;

import lombok.Data;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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

        final Envelope<Order> payload = new Envelope<>(MessageTypes.OrderCreated_v1, order);

        var message = MessageBuilder.withPayload(payload)
                // spring cloud stream is configure to use the value of the topic header
                // as the topic when publishing messages on the output channel
                .setHeader("topic", payload.messageType)
                .build();

        this.source.output().send(message);

        log.info("action: publish | messageId: {} | messageType: {}", payload.id, payload.messageType);
    }

    @Data
    class Envelope<TPAYLOAD> {

        private String id = UUID.randomUUID().toString();
        private String timestamp =  DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
        private String messageType;
        private TPAYLOAD payload;

        public Envelope() {
        }

        public Envelope(String messageType, TPAYLOAD payload) {
            this.messageType = messageType;
            this.payload = payload;
        }
    }

}
