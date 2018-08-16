package net.malevy.edaorder;

import net.malevy.edaorder.config.ExternalServicesProperties;
import net.malevy.edaorder.messages.PaymentApproved;
import net.malevy.edaorder.messages.Seats;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class PaymentGateway {

    ExternalServicesProperties externalServicesProperties;
    private final RestTemplate restTemplate;

    public PaymentGateway(ExternalServicesProperties externalServicesProperties,
                          RestTemplate restTemplate) {
        this.externalServicesProperties = externalServicesProperties;
        this.restTemplate = restTemplate;
    }

    public PaymentApproved approve(Seats seats) {
        String paymentApprovalUrl = UriComponentsBuilder.fromUri(externalServicesProperties.getPayment())
                .path("/payments/accept")
                .toUriString();

        var paymentResponse = this.restTemplate
                .postForEntity(paymentApprovalUrl, seats, PaymentApproved.class);

        return paymentResponse.getBody();

    }
}
