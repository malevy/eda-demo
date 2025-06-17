package net.malevy.edaserviceregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EdaServiceRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdaServiceRegistryApplication.class, args);
    }
}
