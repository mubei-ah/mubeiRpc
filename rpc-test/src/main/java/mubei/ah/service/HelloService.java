package mubei.ah.service;

/**
 * @author 帅小伙呀
 * @date 2023/6/25 21:11
 */
public interface HelloService {

    String hello(String name);

    String hello(Person person);

    String hello(String name, Integer age);
}
