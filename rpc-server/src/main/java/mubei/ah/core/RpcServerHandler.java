package mubei.ah.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import mubei.ah.codec.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 帅小伙呀
 * @date 2023/5/17 21:52
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    private final Map<String, Object> handlerMap;
    private final ThreadPoolExecutor serverHandlerPool;

    public RpcServerHandler(Map<String, Object> handlerMap, final ThreadPoolExecutor threadPoolExecutor) {
        this.handlerMap = handlerMap;
        this.serverHandlerPool = threadPoolExecutor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {

    }
}
