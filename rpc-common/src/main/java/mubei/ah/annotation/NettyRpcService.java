package mubei.ah.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author 帅小伙呀
 * @date 2023/5/17 21:30
 * RPC annotation for RPC service
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface NettyRpcService {
    Class<?> value();

    String version() default "";
}
