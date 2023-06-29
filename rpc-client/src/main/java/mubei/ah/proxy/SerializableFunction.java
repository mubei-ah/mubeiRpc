package mubei.ah.proxy;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * @author 帅小伙呀
 * @date 2023/6/27 17:03
 */
public interface SerializableFunction<T> extends Serializable {

    default String getName() throws Exception{
        Method write = this.getClass().getDeclaredMethod("writeReplace");
        write.setAccessible(true);
        SerializedLambda serializedLambda = (SerializedLambda) write.invoke(this);
        return serializedLambda.getImplMethodName();
    }
}
