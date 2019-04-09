package org.ms.openfeignflux;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zhenglai
 * @since 2019-04-09 23:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServerInfo {
    private String url;
}
