package mubei.ah.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import mubei.ah.codec.*;
import mubei.ah.serializer.Serializer;
import mubei.ah.serializer.kryo.KryoSerializer;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 帅小伙呀
 * @date 2023/5/17 21:43
 *
 * 处理rpc的channel 传入服务以及线程池
 */
public class RpcServerInitializer extends ChannelInitializer<SocketChannel> {
    private Map<String,Object> handlerMap;
    private ThreadPoolExecutor threadPoolExecutor;


    public RpcServerInitializer(Map<String, Object> handlerMap, ThreadPoolExecutor threadPoolExecutor) {
        this.handlerMap = handlerMap;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
//        Serializer serializer = ProtostuffSerializer.class.newInstance();
//        Serializer serializer = HessianSerializer.class.newInstance();
        Serializer serializer = KryoSerializer.class.newInstance();
        ChannelPipeline cp = channel.pipeline();
        cp
                .addLast(new IdleStateHandler(0,0, Beat.BEAT_TIMEOUT, TimeUnit.SECONDS))
                // 编码器
                .addLast(new RpcEncoder(RpcRequest.class, null))
                .addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,0))
                // 解码器
                .addLast(new RpcDecoder(RpcRequest.class, serializer))
                .addLast(new RpcEncoder(RpcResponse.class, serializer))
                .addLast(new RpcServerHandler(handlerMap,threadPoolExecutor))
               ;
    }
}
