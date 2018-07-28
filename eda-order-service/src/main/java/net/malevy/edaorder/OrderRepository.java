package net.malevy.edaorder;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OrderRepository {

    private final static ConcurrentHashMap<String, Order> storage = new ConcurrentHashMap<>();

    public Order save(Order order) {
        Assert.notNull(order, "must supply an order");

        final ModelMapper mapper = new ModelMapper();
        final Order clone = mapper.map(order, Order.class);

        if (StringUtils.isEmpty(clone.getId())) {
            clone.setId(UUID.randomUUID().toString());
            clone.setStatus(Order.Statuses.PENDING);
        }

        OrderRepository.storage.put(clone.getId(), clone);

        return clone;
    }

    public Optional<Order> getById(String id) {
        Assert.hasText(id, "must supply an id");

        return Optional.ofNullable(OrderRepository.storage.get(id));
    }

}
