package mubei.ah.service;

import mubei.ah.annotation.NettyRpcService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author 帅小伙呀
 * @date 2023/6/25 21:28
 */
@NettyRpcService(PersonService.class)
public class PersonServiceImpl implements PersonService {

    @Override
    public List<Person> callPerson(String name, int num) {
        return IntStream.range(0,num)
                .mapToObj(i -> new Person(String.valueOf(i),name))
                .collect(Collectors.toList());
    }

}
