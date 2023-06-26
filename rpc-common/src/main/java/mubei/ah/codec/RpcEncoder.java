package mubei.ah.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import mubei.ah.serializer.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 帅小伙呀
 * @date 2023/5/20 17:41
 */
public class RpcEncoder extends MessageToByteEncoder {

    private static final Logger logger = LoggerFactory.getLogger(RpcEncoder.class);

    private Class<?> genericClass;
    private Serializer serializer;



    public RpcEncoder(Class<?> genericClass, Serializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            try {
                byte[] data = serializer.serialize(in);
                out.writeInt(data.length);
                out.writeBytes(data);
            } catch (Exception ex) {
                logger.error("Encode error: " + ex.toString());
            }
        }
    }
}
