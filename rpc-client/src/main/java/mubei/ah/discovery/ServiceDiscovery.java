package mubei.ah.discovery;

import mubei.ah.connect.ConnectionManager;
import mubei.ah.protocol.RpcProtocol;
import mubei.ah.zk.CuratorClient;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author 帅小伙呀
 * @date 2023/7/3 17:15
 * 服务发现
 */
public class ServiceDiscovery {
    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);
    private CuratorClient curatorClient;

    public  ServiceDiscovery(String registryAddress) {
        this.curatorClient = new CuratorClient(registryAddress);
        //discoveryService();
    }

    // TODO: 2023/7/3 完善其他功能



    private void getServiceAndUpdateServer(ChildData childData,PathChildrenCacheEvent.Type type) {
        String path = childData.getPath();
        String data = new String(childData.getData(), StandardCharsets.UTF_8);
        logger.info("Child data updated,path:{},type:{},data:{}",path,type,data);
        RpcProtocol rpcProtocol = RpcProtocol.fromJson(data);
        updateConnectedServer(rpcProtocol,type);
    }

    private void UpdateConnectedServer(List<RpcProtocol> dataList) {
        ConnectionManager.getInstance().updateConnectedServer(dataList);
    }

    private void updateConnectedServer(RpcProtocol rpcProtocol, PathChildrenCacheEvent.Type type) {
        ConnectionManager.getInstance().updateConnectedServer(rpcProtocol, type);
    }


    public void stop() {
        this.curatorClient.close();
    }

}
