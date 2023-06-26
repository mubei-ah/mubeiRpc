package mubei.ah.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import mubei.ah.codec.RpcRequest;
import mubei.ah.codec.RpcResponse;
import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 * @author 帅小伙呀
 * @date 2023/6/5 12:18
 */
public class KryoPoolFactory {
    private static volatile KryoPoolFactory poolFactory = null;

    private KryoFactory factory;

    {
        factory = new KryoFactory() {
            @Override
            public Kryo create() {
                Kryo kryo = new Kryo();
                kryo.setReferences(false);
                kryo.register(RpcRequest.class);
                kryo.register(RpcResponse.class);
                Kryo.DefaultInstantiatorStrategy strategy = (Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy();
                strategy.setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
                return kryo;
            }
        };
    }

    private KryoPool pool = new KryoPool.Builder(factory).build();

    private KryoPoolFactory() {
    }

    public static KryoPool getKryoPoolInstance() {
        if (poolFactory == null) {
            synchronized (KryoPoolFactory.class) {
                if (poolFactory == null) {
                    poolFactory = new KryoPoolFactory();
                }
            }
        }
        return poolFactory.getPool();
    }

    public KryoPool getPool() {
        return pool;
    }

}
