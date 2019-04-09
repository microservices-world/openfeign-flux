package org.ms.openfeignflux.resthandlers;

import org.ms.openfeignflux.MethodInfo;
import org.ms.openfeignflux.RestHandler;
import org.ms.openfeignflux.ServerInfo;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

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
        ResponseSpec request = client
                .method(methodInfo.getHttpMethod())
                .uri(methodInfo.getUrl(), methodInfo.getParams())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve();
        if (methodInfo.isReturnFlux()) {
            result = request.bodyToFlux(methodInfo.getReturnElementType());
        } else {
            result = request.bodyToMono(methodInfo.getReturnElementType());
        }
        return result;
    }
}
