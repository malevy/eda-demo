package net.malevy.edaorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;

@SpringBootApplication
//@EnableDiscoveryClient
@EnableBinding(Processor.class)
public class EdaOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdaOrderApplication.class, args);
    }
}
