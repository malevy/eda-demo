package net.malevy.edareservation;

import net.malevy.edareservation.messages.Seats;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ReservationRepository {

    private final CacheManager cacheManager;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ReservationRepository(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void save(final Seats seats) {

        cacheInstance().put(seats.getOrderId(), seats);
    }

    public Optional<Seats> Retrieve(String orderId) {

        return Optional.ofNullable(cacheInstance().get(orderId, Seats.class));
    }

    private Cache cacheInstance() {
        return cacheManager.getCache("reservations");
    }

}
