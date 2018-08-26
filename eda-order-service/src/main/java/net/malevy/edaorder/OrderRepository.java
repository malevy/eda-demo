package net.malevy.edaorder;

import org.modelmapper.ModelMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepository {

    private CacheManager cacheManager;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public OrderRepository(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Order save(Order order) {
        Assert.notNull(order, "must supply an order");

        final var mapper = new ModelMapper();
        final var clone = mapper.map(order, Order.class);

        if (StringUtils.isEmpty(clone.getId())) {
            clone.setId(UUID.randomUUID().toString());
            clone.setStatus(Order.Statuses.PENDING);
        }

        cacheInstance().put(clone.getId(), clone);

        return clone;
    }

    public Optional<Order> getById(String id) {
        Assert.hasText(id, "must supply an id");

        return Optional.ofNullable(cacheInstance().get(id, Order.class)) ;
    }

    private Cache cacheInstance() {
        return cacheManager.getCache("orders");
    }

}
