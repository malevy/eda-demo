package net.malevy.edaorder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class OrderController {

    private OrderRepository orderRepository;
    private Publisher publisher;

    public OrderController(OrderRepository orderRepository,
                           Publisher publisher) {

        this.orderRepository = orderRepository;
        this.publisher = publisher;
    }

    @PostMapping("/orders")
    public Mono<ResponseEntity<?>> createOrder(final @RequestBody Order order,
                                               final UriComponentsBuilder uriComponentsBuilder) {

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
