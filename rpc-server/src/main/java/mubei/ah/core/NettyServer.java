package mubei.ah.core;

import mubei.ah.util.ServiceUtil;
import mubei.ah.util.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 帅小伙呀
 * @date 2023/5/10 21:28
 */
public class NettyServer extends Server {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private Thread thread;
    private String serverAddress;
    //private ServiceRegistry serviceRegistry;
    private Map<String, Object> serviceMap = new HashMap<>();

    public NettyServer(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public NettyServer(String serverAddress, String registryAddress) {
        this.serverAddress = serverAddress;
        // 注册服务的地址
    }


    public void addService(String interfaceName, String version, Object serviceBean) {
        logger.info("Adding service, interface: {}, version: {}, bean：{}", interfaceName, version, serviceBean);
        String serverKey = ServiceUtil.makeServiceKey(interfaceName, version);
        serviceMap.put(serverKey, serviceBean);
    }

    @Override
    public void start() throws Exception {
        new Thread(()->{
            ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtil.createThreadPool(
                    NettyServer.class.getSimpleName(), 16, 32);


        }).start();
    }

    @Override
    public void stop() throws Exception {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }
}
