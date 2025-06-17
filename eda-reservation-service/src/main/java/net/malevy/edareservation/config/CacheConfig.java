package net.malevy.edareservation.config;

import com.hazelcast.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public Config buildCacheConfig() {

        final var config = new Config();
        config.setClusterName("reservation-service-cache");
        return config;

    }
}
