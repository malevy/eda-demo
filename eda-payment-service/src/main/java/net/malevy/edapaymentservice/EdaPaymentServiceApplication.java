package net.malevy.edapaymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;

@SpringBootApplication
@EnableBinding(Processor.class)
@EnableDiscoveryClient
public class EdaPaymentServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(EdaPaymentServiceApplication.class, args);
    }
}
