package net.malevy.edaorder;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@RestController
public class OrderController {

    private OrderRepository orderRepository;
    private Publisher publisher;

    public OrderController(OrderRepository orderRepository, Publisher publisher) {

        this.orderRepository = orderRepository;
        this.publisher = publisher;
    }

    @PostMapping("/orders")
    public Mono<ResponseEntity<?>> createOrder(@RequestBody Order order, UriComponentsBuilder uriComponentsBuilder) {

        if (null == order) return Mono.just( ResponseEntity
                .badRequest()
                .body("no order was supplied"));

        final Order committedOrder = orderRepository.save(order);
        uriComponentsBuilder
                .replacePath("order")
                .pathSegment(order.getId());

        ResponseEntity<Order> response = ResponseEntity
                .created(uriComponentsBuilder.build().toUri())
                .body(committedOrder);

        publisher.orderCreated(committedOrder);

        return Mono.just(response);
    }

}
