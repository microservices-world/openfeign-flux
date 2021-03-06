package org.ms.openfeignflux;

import org.ms.openfeignflux.interfaces.ProxyCreator;
import org.ms.openfeignflux.proxies.JDKProxyCreator;
import org.ms.openfeignflux.test.IUserApi;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OpenfeignFluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenfeignFluxApplication.class, args);
    }

    @Bean
    ProxyCreator proxyCreator() {
        return new JDKProxyCreator();
    }

    @Bean
    FactoryBean<IUserApi> userApi(ProxyCreator proxyCreator) {
        return new FactoryBean<>() {
            @Override
            public IUserApi getObject() throws Exception {
                return (IUserApi) proxyCreator.createProxy(getObjectType());
            }

            @Override
            public Class<?> getObjectType() {
                return IUserApi.class;
            }
        };
    }
}
