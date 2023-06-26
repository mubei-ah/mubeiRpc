package mubei.ah.service;

import mubei.ah.annotation.NettyRpcService;

/**
 * @author 帅小伙呀
 * @date 2023/6/25 21:19
 */
@NettyRpcService(value = HelloService.class,version = "2.0")
public class HelloServiceImpl2 implements  HelloService{

    public HelloServiceImpl2() {

    }

    @Override
    public String hello(String name) {
        return "Hi " + name;
    }

    @Override
    public String hello(Person person) {
        return "Hi " + person.getFirstName() + " " + person.getLastName();
    }

    @Override
    public String hello(String name, Integer age) {
        return name + " is " + age;
    }
}
