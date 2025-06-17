package net.malevy.edapaymentservice;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.malevy.edapaymentservice.messages.PaymentApproved;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PaymentController {


    @PostMapping("/payments/accept")
    public ResponseEntity<?> accept(final @RequestBody AcceptRequestBody request) {

        final var approvedMessage = new PaymentApproved(
                request.orderId,
                request.showId,
                RandomStringUtils.randomAlphanumeric(12),
                "payment approved");

        log.info("action: payment-processed | orderId: {}", request.orderId);

        return ResponseEntity.ok(approvedMessage);
    }

    @Value
    static class AcceptRequestBody {
        public String orderId;
        public String showId;
    }
}


