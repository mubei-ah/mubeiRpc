package mubei.ah.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import mubei.ah.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author 帅小伙呀
 * @date 2023/6/5 11:37
 */
public class KryoSerializer extends Serializer {
    private KryoPool pool = KryoPoolFactory.getKryoPoolInstance();

    @Override
    public <T> byte[] serialize(T obj) {
        Kryo kryo = pool.borrow();
        try (
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Output out = new Output(byteArrayOutputStream)
        ) {
            kryo.writeObject(out, obj);
            out.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            pool.release(kryo);
        }
    }

    @Override
    public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
        Kryo kryo = pool.borrow();
        try (
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                Input input = new Input(byteArrayInputStream);
        ) {
            Object result = kryo.readObject(input, clazz);
            input.close();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            pool.release(kryo);
        }
    }
}
