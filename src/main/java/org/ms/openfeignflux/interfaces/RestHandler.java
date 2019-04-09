package org.ms.openfeignflux.interfaces;


import org.ms.openfeignflux.beans.MethodInfo;
import org.ms.openfeignflux.beans.ServerInfo;

/**
 * @author Zhenglai
 * @since 2019-04-09 23:37
 */
public interface RestHandler {

    /**
     * Make the real rest call and get response
     * @param methodInfo
     * @return
     */
    Object invokeRest(MethodInfo methodInfo);

    /**
     * init server info
     * @param serverInfo
     */
    void init(ServerInfo serverInfo);
}
