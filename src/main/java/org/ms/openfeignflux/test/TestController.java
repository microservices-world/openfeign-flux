package org.ms.openfeignflux.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
//        var users = userApi.getAllUser();
//        userApi.getUser("5cab8a5c8b0b783897fd2e19");
        userApi.deleteUser("5cab8a7c8b0b783897fd2e1a").subscribe(x -> System.out.println("delete success"));
//        userApi.createUser(Mono.just(new User()));
//        users.subscribe(System.out::println);
    }
}
