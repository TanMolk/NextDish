package ncl.csc8019.group12.delay;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.DelayQueue;

/**
 * @author wei tan
 */
public class DelayTest {

    @Test
    public void testDelayTask() throws InterruptedException {
        long start = System.currentTimeMillis();


        long delayMillisecond = 10000;

        DelayQueue<NormalDelayTask> queue = new DelayQueue<>();
        queue.add(new NormalDelayTask("1",delayMillisecond));
        queue.take();
        long end = System.currentTimeMillis();
        Assert.assertTrue(end - start >= delayMillisecond);
    }
}
