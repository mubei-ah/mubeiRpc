package mubei.ah.codec;

import java.io.Serializable;

/**
 * @author 帅小伙呀
 * @date 2023/6/5 12:19
 */
public class RpcResponse implements Serializable {
    private static final long serialVersionUID = 821549332945L;


    private String requestId;
    private String error;
    private Object result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
