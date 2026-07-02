package lld.ticketmaster.service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Manages TTL-based expiry for pending bookings.
 *
 * We take a Runnable callback from BookingService to avoid a circular dependency.
 * The callback handles seat release + booking state transition.
 *
 * Interview note: In production this is Redis key expiry + a Keyspace Notification
 * subscriber, not a local scheduler. The local scheduler works fine for a single node.
 */
public class SeatLockService {

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(2, r -> {
                Thread t = new Thread(r, "seat-lock-expiry");
                t.setDaemon(true);
                return t;
            });

    private final Map<String, ScheduledFuture<?>> expiryTasks = new ConcurrentHashMap<>();

    public void scheduleExpiry(String bookingId, Runnable onExpire, Duration duration) {
        ScheduledFuture<?> future = scheduler.schedule(
                onExpire,
                duration.toSeconds(),
                TimeUnit.SECONDS
        );
        expiryTasks.put(bookingId, future);
    }

    public void cancelExpiry(String bookingId) {
        ScheduledFuture<?> future = expiryTasks.remove(bookingId);
        if (future != null) future.cancel(false);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}