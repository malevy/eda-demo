package net.malevy.edaorder;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CountAggregator {
    private final AtomicInteger createdCount = new AtomicInteger(0);
    private final AtomicInteger completedCount = new AtomicInteger(0);

    public void recordCreated() {createdCount.getAndIncrement(); }
    public void recordCompleted() {completedCount.getAndIncrement(); }

    public int currentCreatedCount() { return createdCount.get(); }
    public int currentCompletedCount() { return completedCount.get(); }

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
