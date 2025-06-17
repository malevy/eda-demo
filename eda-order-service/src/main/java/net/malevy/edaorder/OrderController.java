package net.malevy.edaorder;

import lombok.extern.slf4j.Slf4j;
import net.malevy.edaorder.messages.Order;
import net.malevy.edaorder.messages.Seats;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@Slf4j
public class OrderController {

    private final OrderRepository orderRepository;
    private final ReservationGateway reservationGateway;
    private final PaymentGateway paymentGateway;
    private final Publisher publisher;
    private final CountAggregator countAggregator;

    public OrderController(OrderRepository orderRepository,
                           ReservationGateway reservationGateway,
                           PaymentGateway paymentGateway,
                           Publisher publisher,
                           CountAggregator countAggregator) {

        this.orderRepository = orderRepository;
        this.reservationGateway = reservationGateway;
        this.paymentGateway = paymentGateway;
        this.publisher = publisher;
        this.countAggregator = countAggregator;
    }

    @PostMapping("/orders/sync")
    public ResponseEntity<?> createOrderSync(final @RequestBody Order order,
                                               final UriComponentsBuilder uriComponentsBuilder) {

        if (null == order) return ResponseEntity
                .badRequest()
                .body("no order was supplied");

        final var committedOrder = orderRepository.save(order);
        countAggregator.recordCreated();

        uriComponentsBuilder
                .replacePath("orders")
                .pathSegment(order.getId());

        // reserve seats
        final var reservationRequest = new Seats(order.getId(),
                order.getShowId(),
                "",
                order.getSeats());

        var reservation = reservationGateway.reserve(reservationRequest);

        // process payment
        var paymentResponse = paymentGateway.approve(reservation);

        // complete reservation
        reservationGateway.complete(reservation.getOrderId());

        // complete order
        committedOrder.setStatus(Order.Statuses.COMPLETED);
        this.orderRepository.save(committedOrder);
        countAggregator.recordCompleted();

        return ResponseEntity
                .created(uriComponentsBuilder.build().toUri())
                .body(committedOrder);
    }

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(final @RequestBody Order order,
                                               final UriComponentsBuilder uriComponentsBuilder) {

        if (null == order) return ResponseEntity
                .badRequest()
                .body("no order was supplied");

        final Order committedOrder = orderRepository.save(order);
        countAggregator.recordCreated();

        uriComponentsBuilder
                .replacePath("orders")
                .pathSegment(order.getId());

        ResponseEntity<Order> response = ResponseEntity
                .created(uriComponentsBuilder.build().toUri())
                .body(committedOrder);

        publisher.orderCreated(committedOrder);

        return response;
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<?> fetchOrder(@PathVariable("id") final String id) {
        log.info("fetching order {}", id);
        if (!StringUtils.hasText(id)) return ResponseEntity.badRequest().body("missing ID");

        return orderRepository.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/orders/counts")
    public ResponseEntity<?> fetchCounts() {
        log.info("fetching order status count");

        return ResponseEntity.ok(countAggregator.getCounts());
    }

    @DeleteMapping("/orders/counts")
    public ResponseEntity<?> clearCounts() {
        log.info("clearing counts");
        countAggregator.reset();

        return ResponseEntity.ok("counts have been reset");
    }

}
