package mubei.ah.proxy;

/**
 * @author 帅小伙呀
 * @date 2023/6/27 17:05
 */
@FunctionalInterface
public interface RpcFunction<T, P> extends SerializableFunction<T> {

    Object apply(T t, P p);
}
