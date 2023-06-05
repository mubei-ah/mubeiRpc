package mubei.ah.serializer;

/**
 * @author 帅小伙呀
 * @date 2023/5/20 17:43
 */
public abstract class Serializer {

    public abstract <T>byte[] serialize(T obj);
    public abstract <T>Object deserialize(byte[] bytes,Class<T> clazz);

}
