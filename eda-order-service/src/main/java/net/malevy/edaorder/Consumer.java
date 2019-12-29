package net.malevy.edaorder;

import lombok.extern.slf4j.Slf4j;
import net.malevy.edaorder.messages.Envelope;
import net.malevy.edaorder.messages.Seats;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.Payload;
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

    @StreamListener(target = Processor.INPUT, condition = "headers['message-type']=='ticketing.seats.assigned.v1'")
    public void seatsAssignedHandlerHandler(final @Payload Envelope<Seats> envelope) {
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
