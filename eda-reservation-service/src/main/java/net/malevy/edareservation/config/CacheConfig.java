package net.malevy.edareservation.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public Config buildCacheConfig() {
        Config config = new Config();
        GroupConfig groupConfig = config.getGroupConfig();
        groupConfig.setName("reservation-service-cache");
        return config;

    }
}
