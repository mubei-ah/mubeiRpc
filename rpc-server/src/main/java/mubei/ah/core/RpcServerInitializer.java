package mubei.ah.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import mubei.ah.codec.Beat;

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

        ChannelPipeline cp = channel.pipeline();
        cp
                .addLast(new IdleStateHandler(0,0, Beat.BEAT_TIMEOUT, TimeUnit.SECONDS))
                .addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,0))
                // 编解码器
                .addLast(new RpcServerHandler(handlerMap,threadPoolExecutor))
               ;

    }
}
