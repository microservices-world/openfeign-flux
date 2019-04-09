package org.ms.openfeignflux.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Zhenglai
 * @since 2019-04-09 23:22
 */
@RestController
public class TestController {

    @Autowired
    private IUserApi userApi;

    @GetMapping("/")
    public void test() {
        var users = userApi.getAllUser();
        userApi.getUser("12313");
        userApi.deleteUser("3121");
        userApi.createUser(Mono.just(new User()));
        users.subscribe(System.out::println);
    }
}
