package org.ms.openfeignflux.proxies;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import org.ms.openfeignflux.ApiServer;
import org.ms.openfeignflux.beans.MethodInfo;
import org.ms.openfeignflux.beans.ServerInfo;
import org.ms.openfeignflux.interfaces.ProxyCreator;
import org.ms.openfeignflux.interfaces.RestHandler;
import org.ms.openfeignflux.resthandlers.WebClientRestHandler;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Use Jdk proxy to create proxy class
 * 
 * @author Zhenglai
 * @since 2019-04-09 23:31
 */
@Slf4j
public class JDKProxyCreator implements ProxyCreator {

    @Override
    public Object createProxy(final Class<?> type) {
        log.info("createProxy: " + type);
        ServerInfo serverInfo = extractServerInfo(type);
        log.info("serverInfo:" + serverInfo);
        RestHandler handler = new WebClientRestHandler();

        // init server info & web client
        handler.init(serverInfo);
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] {type}, new InvocationHandler() {
            @Override
            public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                // get calling info from method and params
                MethodInfo methodInfo = extractMethodInfo(method, args);
                log.info("methodInfo:" + methodInfo);
                return handler.invokeRest(methodInfo);
            }

            /**
             * Extract calling info from method def. and params
             * @param method
             * @param args
             * @return
             */
            private MethodInfo extractMethodInfo(final Method method, final Object[] args) {
                var methodInfo = new MethodInfo();
                extractUrlAndMethod(method, methodInfo);
                extractReturnType(method, methodInfo);
                extractRequestParamsAndBody(method, args, methodInfo);
                return methodInfo;
            }

            private void extractReturnType(final Method method, final MethodInfo methodInfo) {
                boolean isFlux = method.getReturnType().isAssignableFrom(Flux.class);
                methodInfo.setReturnFlux(isFlux);
                Class<?> elementType = extractElementType(method.getGenericReturnType());
                methodInfo.setReturnElementType(elementType);
            }

            private Class<?> extractElementType(final Type genericReturnType) {
                Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
                return (Class<?>) actualTypeArguments[0];
            }

            private void extractRequestParamsAndBody(final Method method, final Object[] args, final MethodInfo methodInfo) {
                Parameter[] parameters = method.getParameters();
                Map<String, Object> params = new LinkedHashMap<>();
                methodInfo.setParams(params);
                for (int i = 0; i < parameters.length; i++) {
                    PathVariable pathAnnotation = parameters[i].getAnnotation(PathVariable.class);
                    if (pathAnnotation != null) {
                        params.put(pathAnnotation.value(), args[i]);
                    }

                    RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
                    if (requestBody != null) {
                        methodInfo.setBody((Mono<?>) args[i]);
                        methodInfo.setBodyElementType(extractElementType(parameters[i].getParameterizedType()));
                    }
                }
            }

            private void extractUrlAndMethod(final Method method, final MethodInfo methodInfo) {
                Annotation[] annotations = method.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof GetMapping) {
                        GetMapping mapping = (GetMapping) annotation;
                        methodInfo.setUrl(mapping.value()[0]);
                        methodInfo.setHttpMethod(HttpMethod.GET);
                    } else if (annotation instanceof DeleteMapping) {
                        DeleteMapping mapping = (DeleteMapping) annotation;
                        methodInfo.setUrl(mapping.value()[0]);
                        methodInfo.setHttpMethod(HttpMethod.DELETE);
                    } else if (annotation instanceof PostMapping) {
                        PostMapping mapping = (PostMapping) annotation;
                        methodInfo.setUrl(mapping.value()[0]);
                        methodInfo.setHttpMethod(HttpMethod.POST);
                    } else if (annotation instanceof PutMapping) {
                        PutMapping mapping = (PutMapping) annotation;
                        methodInfo.setUrl(mapping.value()[0]);
                        methodInfo.setHttpMethod(HttpMethod.PUT);
                    }

                }
            }
        });
    }

    /**
     * Extract server info
     * @param type
     * @return
     */
    private ServerInfo extractServerInfo(final Class<?> type) {
        var serverInfo = new ServerInfo();
        var apiServer = type.getAnnotation(ApiServer.class);
        serverInfo.setUrl(apiServer.value());
        return serverInfo;
    }
}
