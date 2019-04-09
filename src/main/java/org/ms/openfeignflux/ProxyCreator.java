package org.ms.openfeignflux;

/**
 * 创建代理类接口
 * @author Zhenglai
 * @since 2019-04-09 23:28
 */
public interface ProxyCreator {

    /**
     * Create proxy class
     * @param type
     * @return
     */
    Object createProxy(Class<?> type);
}
