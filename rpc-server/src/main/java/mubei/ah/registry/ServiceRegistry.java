package mubei.ah.registry;

import mubei.ah.config.Constant;
import mubei.ah.protocol.RpcProtocol;
import mubei.ah.protocol.RpcServiceInfo;
import mubei.ah.util.ServiceUtil;
import mubei.ah.zk.CuratorClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 帅小伙呀
 * @date 2023/5/11 14:54
 * 将服务注册到zk中
 */
public class ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);
    private CuratorClient curatorClient;
    private List<String> pathList = new ArrayList<>();

    public ServiceRegistry(String registerAddress) {
        this.curatorClient = new CuratorClient(registerAddress);
    }

    // 注册服务 map注册到zk中
    public void registerService(String host, int port, Map<String, Object> serviceMap) {
        var serviceInfoList = new ArrayList<RpcServiceInfo>();
        for (String key : serviceMap.keySet()) {
            var serviceInfo = key.split(ServiceUtil.SERVICE_CONCAT_TOKEN);
            if (serviceInfo.length > 0) {
                var rpcServiceInfo = new RpcServiceInfo();
                rpcServiceInfo.setServiceName(serviceInfo[0]);
                rpcServiceInfo.setVersion(
                        serviceInfo.length == 2 ? serviceInfo[1] : "");
                logger.info("Register new service: {} ", key);
                serviceInfoList.add(rpcServiceInfo);
            } else {
                logger.warn("Can not get service name and version: {} ", key);
            }
        }
        try {
            var rpcProtocol = new RpcProtocol();
            rpcProtocol.setHost(host);
            rpcProtocol.setPort(port);
            rpcProtocol.setServiceInfoList(serviceInfoList);
            String serviceData = rpcProtocol.toJson();
            byte[] bytes = serviceData.getBytes(StandardCharsets.UTF_8);
            String path = Constant.ZK_DATA_PATH + "-" + rpcProtocol.hashCode();
            path = this.curatorClient.createPathData(path, bytes);
            pathList.add(path);
            logger.info("Register {} new service, host: {}, port: {}", serviceInfoList.size(), host, port);
        } catch (Exception e) {
            logger.error("Register service fail, exception: {}", e.getMessage());
        }
    }


    // 删除所有的服务
    public void unregisterService() {
        logger.info("Unregister all service");
        for (String path : pathList) {
            try {
                this.curatorClient.deletePath(path);
            } catch (Exception ex) {
                logger.error("Delete service path error: " + ex.getMessage());
            }
        }
        this.curatorClient.close();
    }

}
