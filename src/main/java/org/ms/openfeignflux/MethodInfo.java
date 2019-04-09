package org.ms.openfeignflux;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

/**
 * @author Zhenglai
 * @since 2019-04-09 23:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodInfo {
    private String url;
    private HttpMethod httpMethod;
    private Map<String, Object> params;
    private Mono<?> body;
    private boolean returnFlux;
    private Class<?> returnElementType;
}
