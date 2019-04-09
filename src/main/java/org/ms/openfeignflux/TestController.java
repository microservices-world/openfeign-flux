package org.ms.openfeignflux;

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
        var users = userApi.getAllUser();
        users.subscribe(System.out::println);
    }
}
