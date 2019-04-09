package org.ms.openfeignflux.test;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类名随意，字段需要保持一致
 * @author Zhenglai
 * @since 2019-04-09 23:18
 */
@Data
@NoArgsConstructor
public class User {
    private String id;
    private String name;
    private Integer age;
}
