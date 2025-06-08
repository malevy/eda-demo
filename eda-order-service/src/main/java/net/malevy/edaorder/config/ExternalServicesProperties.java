package net.malevy.edaorder.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
@ConfigurationProperties(prefix = "app.services")
@Getter
@Setter
@Slf4j
public class ExternalServicesProperties {

    @NotNull
    private URI payment;

    @NotNull
    private URI reservation;

}

