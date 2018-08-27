package net.malevy.edaorder;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CountAggregator {
    private final AtomicInteger createdCount = new AtomicInteger(0);
    private final AtomicInteger completedCount = new AtomicInteger(0);

    public void recordCreated() {createdCount.getAndIncrement(); }
    public void recordCompleted() {completedCount.getAndIncrement(); }

    private int currentCreatedCount() { return createdCount.get(); }
    private int currentCompletedCount() { return completedCount.get(); }

    public OrderStatusCounts getCounts() {
        return new OrderStatusCounts(currentCreatedCount(), currentCompletedCount());
    }

    public void reset() {
        createdCount.set(0);
        completedCount.set(0);
    }

    @Getter
    class OrderStatusCounts {
        private final int createdCount;
        private final int completedCount;

        public OrderStatusCounts(int createdCount, int completedCount) {
            this.createdCount = createdCount;
            this.completedCount = completedCount;
        }


    }


}
