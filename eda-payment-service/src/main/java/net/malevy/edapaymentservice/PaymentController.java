package net.malevy.edapaymentservice;

import lombok.extern.slf4j.Slf4j;
import net.malevy.edapaymentservice.messages.PaymentApproved;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class PaymentController {

    @PostMapping("/payments/accept")
    public Mono<ResponseEntity<?>> accept(final @Payload String orderId,
                                          final @Payload String showId) {

        final var approvedMessage = new PaymentApproved(
                orderId,
                showId,
                RandomStringUtils.randomAlphanumeric(12),
                "payment approved");

        log.info("action: payment-processed | orderId: {}", orderId);

        return Mono.just(ResponseEntity.ok(approvedMessage));
    }

}
