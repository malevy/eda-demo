package net.malevy.edaorder.config;

import com.hazelcast.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public Config buildCacheConfig() {
        Config config = new Config();
        config.setClusterName("order-service-cache");
        return config;

    }
}
