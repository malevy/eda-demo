package net.malevy.edareservation;

import net.malevy.edareservation.messages.Order;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class OrderRepository {

    private final HashMap<String, Order> storage = new HashMap<>();

    public void Register(final Order order) {
        storage.put(order.getId(), order);
    }

    public Optional<Order> Retrieve(String orderId) {
        return Optional.ofNullable(storage.get(orderId));
    }

}
