package mubei.ah.server;

import mubei.ah.RpcServer;
import mubei.ah.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author 帅小伙呀
 * @date 2023/6/25 20:43
 */
public class RpcServerBootstrap {
    private static final Logger logger = LoggerFactory.getLogger(RpcServerBootstrap.class);

    public static void main(String[] args) {
        //
        String serverAddress = "127.0.0.1:18877";
        String registryAddress = "10.217.59.164:2181";
        RpcServer rpcServer = new RpcServer(serverAddress,registryAddress);
        HelloServiceImpl helloService1 = new HelloServiceImpl();
        rpcServer.addService(HelloService.class.getName(),"1.0",helloService1);
        HelloServiceImpl2 helloService2 = new HelloServiceImpl2();
        rpcServer.addService(HelloService.class.getName(),"2.0",helloService2);
        PersonService personService = new PersonServiceImpl();
        rpcServer.addService(PersonService.class.getName(), "", personService);
        try {
            rpcServer.start();
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.toString());
        }
    }
}
