package mubei.ah.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 帅小伙呀
 * @date 2023/5/10 21:52
 */
public class ThreadPoolUtil {

    public static ThreadPoolExecutor createThreadPool(final String name, int corePoolSize, int maxPoolSize) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                r -> new Thread(r, "mubei-rpc-" + name + "-" + r.hashCode()),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
