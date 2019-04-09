package org.ms.openfeignflux.resthandlers;

import jdk.jshell.Snippet.Status;
import org.ms.openfeignflux.MethodInfo;
import org.ms.openfeignflux.RestHandler;
import org.ms.openfeignflux.ServerInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

/**
 * @author Zhenglai
 * @since 2019-04-10 00:00
 */
public class WebClientRestHandler implements RestHandler {

    private WebClient client;

    @Override
    public void init(final ServerInfo serverInfo) {
        client = WebClient.create(serverInfo.getUrl());
    }

    @Override
    public Object invokeRest(final MethodInfo methodInfo) {
        Object result;
        RequestBodySpec accept = client
                .method(methodInfo.getHttpMethod())
                .uri(methodInfo.getUrl(), methodInfo.getParams())
                .accept(MediaType.APPLICATION_JSON_UTF8);
        ResponseSpec retrieve;
        if (methodInfo.getBody() != null) {
            retrieve = accept.body(methodInfo.getBody(), methodInfo.getBodyElementType()).retrieve();
        } else {
            retrieve= accept
                .retrieve();
        }

        retrieve.onStatus(status -> status == HttpStatus.NOT_FOUND, resp -> Mono.just(new RuntimeException("Not Found")));

        if (methodInfo.isReturnFlux()) {
            result = retrieve.bodyToFlux(methodInfo.getReturnElementType());
        } else {
            result = retrieve.bodyToMono(methodInfo.getReturnElementType());
        }
        return result;
    }
}
