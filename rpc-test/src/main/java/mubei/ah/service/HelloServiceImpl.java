package mubei.ah.service;

import mubei.ah.annotation.NettyRpcService;

/**
 * @author 帅小伙呀
 * @date 2023/6/25 21:16
 */
@NettyRpcService(value = HelloService.class,version = "1.0")
public class HelloServiceImpl implements  HelloService{

    public HelloServiceImpl() {

    }

    @Override
    public String hello(String name) {
        return "Hello " + name;
    }

    @Override
    public String hello(Person person) {
        return "Hello " + person.getFirstName() + " " + person.getLastName();
    }

    @Override
    public String hello(String name, Integer age) {
        return name + " is " + age;
    }
}
