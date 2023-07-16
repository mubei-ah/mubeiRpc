package mubei.ah.connect;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import mubei.ah.handler.RpcClientHandler;
import mubei.ah.protocol.RpcProtocol;
import mubei.ah.route.RpcLoadBalance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 帅小伙呀
 * @date 2023/6/26 17:26
 */
public class ConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(4, 8, 600L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000));
    private static Map<RpcProtocol, RpcClientHandler> connectedServerNodes = new ConcurrentHashMap<>();
    private static CopyOnWriteArraySet<RpcProtocol> rpcProtocolSet = new CopyOnWriteArraySet<>();

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition connected = lock.newCondition();
    private static final long WAIT_TIMEOUT = 5000L;
    // TODO: 2023/6/26 rpc 服务路由
    private RpcLoadBalance loadBalance = null;
    private volatile boolean isRunning = true;

    // 获取单例
    private ConnectionManager() {
    }

    private static class SingletonHolder {
        private static final ConnectionManager instance = new ConnectionManager();
    }

    public static ConnectionManager getInstance() {
        return SingletonHolder.instance;
    }

    public void updateConnectedServer(List<RpcProtocol> serviceList) {
        if (serviceList != null && serviceList.size() > 0) {
            HashSet<RpcProtocol> serviceSet = new HashSet<>(serviceList.size());
            for (RpcProtocol rpcProtocol : serviceList) {
                serviceSet.add(rpcProtocol);
            }

            for (RpcProtocol rpcProtocol : serviceSet) {
                if (!rpcProtocolSet.contains(rpcProtocol)) {
                    // TODO: 2023/7/3  连接服务
                    connectServerNode(rpcProtocol);
                }
            }

            // Close and remove invalid server nodes
            for (RpcProtocol rpcProtocol : rpcProtocolSet) {
                if (!serviceSet.contains(rpcProtocol)) {
                    logger.info("Remove invalid service: " + rpcProtocol.toJson());
                    removeAndCloseHandler(rpcProtocol);
                }
            }
        } else {
            logger.error("No available service!");
            for (RpcProtocol rpcProtocol : rpcProtocolSet) {
                removeAndCloseHandler(rpcProtocol);
            }
        }
    }

    public void updateConnectedServer(RpcProtocol rpcProtocol, PathChildrenCacheEvent.Type type) {

    }


    private void signalAvailableHandler() {
        lock.lock();
        try {
            connected.signalAll();
        } finally {
            lock.unlock();
        }
    }


    public RpcClientHandler chooseHandler(String serviceKey) throws Exception {
        while (isRunning && connectedServerNodes.isEmpty()) {
            try {
                waitingForHandler();
            } catch (InterruptedException e) {
                logger.error("Waiting for available service is interrupted!", e);
            }
        }
        RpcProtocol rpcProtocol = loadBalance.route(serviceKey, connectedServerNodes);
        RpcClientHandler handler = connectedServerNodes.get(rpcProtocol);
        if (handler != null) {
            return handler;
        } else {
            throw new Exception("Can not get available connection");
        }
    }


    private void waitingForHandler() throws InterruptedException {
        lock.lock();
        try {
            logger.warn("waiting for available service");
            connected.await(WAIT_TIMEOUT, TimeUnit.MICROSECONDS);
        } finally {
            lock.unlock();
        }
    }

    private void removeAndCloseHandler(RpcProtocol rpcProtocol) {
        RpcClientHandler handler = connectedServerNodes.get(rpcProtocol);
        if (handler != null) {
            handler.close();
        }
        connectedServerNodes.remove(rpcProtocol);
        rpcProtocolSet.remove(rpcProtocol);
    }

    // 停止服务
    public void stop() {
        isRunning = false;
        for (RpcProtocol rpcProtocol : rpcProtocolSet) {
            removeAndCloseHandler(rpcProtocol);
        }
        signalAvailableHandler();
        THREAD_POOL_EXECUTOR.shutdown();
        eventLoopGroup.shutdownGracefully();
    }


}
