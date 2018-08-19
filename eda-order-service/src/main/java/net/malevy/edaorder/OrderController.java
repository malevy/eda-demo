package net.malevy.edaorder;

import lombok.extern.slf4j.Slf4j;
import net.malevy.edaorder.messages.PaymentApproved;
import net.malevy.edaorder.messages.Seats;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class OrderController {

    private final OrderRepository orderRepository;
    private final ReservationGateway reservationGateway;
    private final PaymentGateway paymentGateway;
    private final Publisher publisher;

    public OrderController(OrderRepository orderRepository,
                           ReservationGateway reservationGateway,
                           PaymentGateway paymentGateway,
                           Publisher publisher,
                           RestTemplate restTemplate) {

        this.orderRepository = orderRepository;
        this.reservationGateway = reservationGateway;
        this.paymentGateway = paymentGateway;
        this.publisher = publisher;
    }

    @PostMapping("/orders/sync")
    public Mono<ResponseEntity<?>> createOrderSync(final @RequestBody Order order,
                                               final UriComponentsBuilder uriComponentsBuilder) {

        if (null == order) return Mono.just( ResponseEntity
                .badRequest()
                .body("no order was supplied"));

        final Order committedOrder = orderRepository.save(order);

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

        ResponseEntity<Order> response = ResponseEntity
                .created(uriComponentsBuilder.build().toUri())
                .body(committedOrder);

        return Mono.just(response);

    }

    @PostMapping("/orders")
    public Mono<ResponseEntity<?>> createOrder(final @RequestBody Order order,
                                               final UriComponentsBuilder uriComponentsBuilder) {

        if (null == order) return Mono.just( ResponseEntity
                .badRequest()
                .body("no order was supplied"));

        final Order committedOrder = orderRepository.save(order);

        uriComponentsBuilder
                .replacePath("orders")
                .pathSegment(order.getId());

        ResponseEntity<Order> response = ResponseEntity
                .created(uriComponentsBuilder.build().toUri())
                .body(committedOrder);

        publisher.orderCreated(committedOrder);

        return Mono.just(response);
    }

    @GetMapping("/orders/{id}")
    public Mono<ResponseEntity<?>> fetchOrder(@PathVariable final String id) {
        log.info("fetching order {}", id);
        if (StringUtils.isEmpty(id)) return Mono.just(
                ResponseEntity.badRequest().body("missing ID")
        );

        var response = orderRepository.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

        return Mono.just(response);
    }

}
