package net.malevy.edaorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableBinding(Processor.class)
@EnableCaching
@EnableConfigurationProperties
public class EdaOrderApplication {

    @Bean
    public RestTemplate buildRestTemplate() {

        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(EdaOrderApplication.class, args);
    }
}
