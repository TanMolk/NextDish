package ncl.csc8019.group12.delay;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author wei tan
 */
public class NormalDelayTask implements Delayed {

    private final String id;
    private final long ttl;

    public NormalDelayTask(String id, long ttl) {
        this.id = id;
        this.ttl = System.currentTimeMillis() + ttl;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long remaining = ttl - System.currentTimeMillis();
        return unit.convert(remaining, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    public String getId() {
        return id;
    }
}
