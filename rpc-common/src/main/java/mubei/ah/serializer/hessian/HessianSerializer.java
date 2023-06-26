package mubei.ah.serializer.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import mubei.ah.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author 帅小伙呀
 * @date 2023/6/5 12:33
 */
public class HessianSerializer extends Serializer {


    @Override
    public <T> byte[] serialize(T obj) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            // 无法自动关闭  不能写在上面
            Hessian2Output ho = new Hessian2Output(os);
            try {
                ho.writeObject(obj);
                ho.flush();
                return os.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException("无法序列化对象", e);
            }
        } catch (IOException e) {
            throw new RuntimeException("无法创建字节数组输出流", e);
        }
    }

    @Override
    public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        ) {
            try {
                Hessian2Input hi = new Hessian2Input(is);
                return hi.readObject();
            } catch (IOException e) {
                throw new RuntimeException("无法序列化对象", e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
