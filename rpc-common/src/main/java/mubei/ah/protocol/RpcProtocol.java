package mubei.ah.protocol;

import mubei.ah.util.JsonUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author 帅小伙呀
 * @date 2023/5/11 17:23
 */
public class RpcProtocol implements Serializable {

    private static final long serialVersionUID = -110218000339519022L;

    private String host;
    private int port;

    private List<RpcServiceInfo> serviceInfoList;

    public String toJson() {
        return JsonUtil.objectToJson(this);
    }

    public static RpcProtocol fromJson(String json) {
        return JsonUtil.jsonToObject(json, RpcProtocol.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RpcProtocol that = (RpcProtocol) o;
        return port == that.port &&
                Objects.equals(host, that.host);
    }


    public int hashCode() {
        return Objects.hash(host, port, serviceInfoList.hashCode());
    }

    @Override
    public String toString() {
        return toJson();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setServiceInfoList(List<RpcServiceInfo> serviceInfoList) {
        this.serviceInfoList = serviceInfoList;
    }

    public List<RpcServiceInfo> getServiceInfoList() {
        return serviceInfoList;
    }
}
