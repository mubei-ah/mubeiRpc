package mubei.ah.route;

import mubei.ah.handler.RpcClientHandler;
import mubei.ah.protocol.RpcProtocol;
import mubei.ah.protocol.RpcServiceInfo;
import mubei.ah.util.ServiceUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 帅小伙呀
 * @date 2023/6/29 14:45
 */
public abstract class RpcLoadBalance {


    protected Map<String, List<RpcProtocol>> getServiceMap(Map<RpcProtocol, RpcClientHandler> connectedServerNodes) {
        Map<String, List<RpcProtocol>> serviceMap = new HashMap<>();
        if (connectedServerNodes != null && !connectedServerNodes.isEmpty()) {
            for (RpcProtocol rpcProtocol : connectedServerNodes.keySet()) {
                for (RpcServiceInfo serviceInfo : rpcProtocol.getServiceInfoList()) {
                    String serviceKey = ServiceUtil.makeServiceKey(serviceInfo.getServiceName(), serviceInfo.getVersion());
                    List<RpcProtocol> rpcProtocolList = serviceMap.computeIfAbsent(serviceKey, k -> new ArrayList<>());
                    rpcProtocolList.add(rpcProtocol);
                }
            }
        }
        return serviceMap;
    }

    protected Map<String, List<RpcProtocol>> getServiceMap2(Map<RpcProtocol, RpcClientHandler> connectedServerNodes) {
        if (connectedServerNodes == null || connectedServerNodes.isEmpty()) {
            return Collections.emptyMap();
        }
        return connectedServerNodes.keySet().stream()
                .flatMap(rpcProtocol -> rpcProtocol.getServiceInfoList().stream()
                        .map(serviceInfo -> {
                            String serviceKey = ServiceUtil.makeServiceKey(serviceInfo.getServiceName(), serviceInfo.getVersion());
                            return new AbstractMap.SimpleEntry<>(serviceKey, rpcProtocol);
                        }))
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    }





    // 根据key 获取服务
    public abstract RpcProtocol route(String serviceKey, Map<RpcProtocol, RpcClientHandler> connectedServerNodes) throws Exception;

}
