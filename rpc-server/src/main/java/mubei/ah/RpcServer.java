package mubei.ah;

import mubei.ah.annotation.NettyRpcService;
import mubei.ah.core.NettyServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import java.util.Map;

/**
 * @author 帅小伙呀
 * @date 2023/5/17 21:24
 */
public class RpcServer extends NettyServer implements ApplicationContextAware, InitializingBean, DisposableBean {
    public RpcServer(String serverAddress, String registryAddress) {
        super(serverAddress, registryAddress);
    }




    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        // 将标注了NettyRpcService 注解的服务加入到注册中心
        Map<String,Object> serviceBeanMap = ctx.getBeansWithAnnotation(NettyRpcService.class);
        if (!serviceBeanMap.isEmpty()) {
            for (Object serviceBean : serviceBeanMap.values()) {
                NettyRpcService nettyRpcService = serviceBean.getClass().getAnnotation(NettyRpcService.class);
                String interfaceName = nettyRpcService.value().getName();
                String version = nettyRpcService.version();
                super.addService(interfaceName,version,serviceBean);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.start();
    }

    @Override
    public void destroy() throws Exception {
        super.stop();
    }
}
