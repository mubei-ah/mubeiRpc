package mubei.ah.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import mubei.ah.registry.ServiceRegistry;
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
    private ServiceRegistry serviceRegistry;
    private Map<String, Object> serviceMap = new HashMap<>();

    public NettyServer(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public NettyServer(String serverAddress, String registryAddress) {
        this.serverAddress = serverAddress;
        // 注册服务的地址
        this.serviceRegistry = new ServiceRegistry(serverAddress);
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
            NioEventLoopGroup bossGroup = new NioEventLoopGroup();
            NioEventLoopGroup workerGroup = new NioEventLoopGroup();

            try {
                ServerBootstrap serverBootstrap = new ServerBootstrap();

                serverBootstrap.group(bossGroup,workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(null)
                        .option(ChannelOption.SO_BACKLOG,128)
                        .childOption(ChannelOption.SO_KEEPALIVE,true);
                String[] array = serverAddress.split(":");
                String host = array[0];
                int port = Integer.parseInt(array[1]);
                ChannelFuture future = serverBootstrap.bind(host,port);
                if (serviceRegistry != null) {
                    serviceRegistry.registerService(host, port, serviceMap);
                }
                logger.info("Server started on port {}", port);
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    logger.info("Rpc server remoting server stop");
                } else {
                    logger.error("Rpc server remoting server error", e);
                }
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        }).start();
    }

    @Override
    public void stop() throws Exception {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }
}
