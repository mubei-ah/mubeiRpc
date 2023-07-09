package mubei.ah.proxy;

/**
 * @author 帅小伙呀
 * @date 2023/6/27 17:20
 */
public interface RpcService<T,P,FN extends SerializableFunction<T>> {

    RpcFunction call(String funcName,Object... args) throws Exception;

    /**
     * lambda method reference
     * @param fn lambda 函数
     * @param args 参数
     */
    RpcFunction call(FN fn,Object... args) throws Exception;
}
