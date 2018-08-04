package net.malevy.edareservation;

import net.malevy.edareservation.messages.Order;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class OrderRepository {

    private CacheManager cacheManager;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public OrderRepository(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void Register(final Order order) {

        cacheInstance().put(order.getId(), order);
    }

    public Optional<Order> Retrieve(String orderId) {

        return Optional.ofNullable(cacheInstance().get(orderId, Order.class));
    }

    private Cache cacheInstance() {
        return cacheManager.getCache("orders");
    }

}
