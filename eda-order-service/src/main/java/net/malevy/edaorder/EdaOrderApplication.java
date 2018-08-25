package net.malevy.edaorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableBinding(Processor.class)
@EnableCaching
@EnableDiscoveryClient
public class EdaOrderApplication {

    @Bean
    @LoadBalanced
    public RestTemplate buildRestTemplate() {

        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(EdaOrderApplication.class, args);
    }
}
