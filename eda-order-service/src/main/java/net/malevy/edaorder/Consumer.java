package net.malevy.edaorder;

import lombok.extern.slf4j.Slf4j;
import net.malevy.edaorder.messages.Envelope;
import net.malevy.edaorder.messages.Order;
import net.malevy.edaorder.messages.Seats;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Consumer {

    private final OrderRepository orderRepository;
    private final CountAggregator countAggregator;

    public Consumer(OrderRepository orderRepository,
                    CountAggregator countAggregator) {
        this.orderRepository = orderRepository;
        this.countAggregator = countAggregator;
    }

    @Bean
    public java.util.function.Consumer<Message<Envelope<Seats>>> seatEventProcessor() {
        return message -> {
            String messageType = (String) message.getHeaders().get("message-type");
            if ("ticketing.seats.assigned.v1".equals(messageType)) {
                seatsAssignedHandler(message.getPayload());
            } else {
                log.error("unexpected message of type {}", messageType);
            }
        };
    }

    private void seatsAssignedHandler(final Envelope<Seats> envelope) {
        log.info("action: received | messageId: {} | messageType: {} | orderId: {}",
                envelope.getId(), envelope.getMessageType(), envelope.getPayload().getOrderId());

        final var seats = envelope.getPayload();

        final var wrappedOrder = this.orderRepository.getById(seats.getOrderId());

        wrappedOrder.ifPresent(order -> {
            order.setStatus(Order.Statuses.COMPLETED);
            this.orderRepository.save(order);
            countAggregator.recordCompleted();
            log.info("action: order-completed | orderId: {}", order.getId());
        });
    }


}
