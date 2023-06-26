package mubei.ah;

import mubei.ah.annotation.RpcAutowired;
import mubei.ah.connect.ConnectionManager;
import mubei.ah.util.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 帅小伙呀
 * @date 2023/6/26 17:59
 */
public class RpcClient implements ApplicationContextAware, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);


    // TODO: 2023/6/26 服务发现


    private static ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtil.createThreadPool(RpcClient.class.getSimpleName(), 8, 16);

    public static <T, P> T createService(Class<T> interfaceClass, String version) {
        // TODO: 2023/6/26 返回代理
        return null;
    }

    public static void submit(Runnable task) {
        threadPoolExecutor.submit(task);
    }

    public void stop() {
        threadPoolExecutor.shutdown();
        // TODO: 2023/6/26 服务关闭
        ConnectionManager.getInstance().stop();
    }

    @Override
    public void destroy() throws Exception {
        this.stop();
    }

    // 设置上下文信息
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        Arrays.stream(beanNames)
                .map(applicationContext::getBean)
                .forEach(bean -> {
                    Field[] fields = bean.getClass().getDeclaredFields();
                    Arrays.stream(fields)
                            .filter(field -> field.isAnnotationPresent(RpcAutowired.class))
                            .forEach(field -> {
                                field.setAccessible(true);
                                RpcAutowired rpcAutowired = field.getAnnotation(RpcAutowired.class);
                                String version = rpcAutowired.version();
                                try {
                                    field.set(bean, createService(field.getType(), version));
                                } catch (IllegalAccessException e) {
                                    logger.error(e.toString());
                                }
                            });
                });
    }
}
