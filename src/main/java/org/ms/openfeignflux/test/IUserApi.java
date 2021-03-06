package org.ms.openfeignflux.test;

import org.ms.openfeignflux.ApiServer;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Zhenglai
 * @since 2019-04-09 23:17
 */
@ApiServer("http://localhost:8080/user")
public interface IUserApi {

    @GetMapping("/")
    Flux<User> getAllUser();

    @GetMapping("/{id}")
    Mono<User> getUser(@PathVariable("id") String id);

    @DeleteMapping("/{id}")
    Mono<Void> deleteUser(@PathVariable("id") String id);

    @PostMapping("/")
    Mono<User> createUser(@RequestBody Mono<User> user);
}
