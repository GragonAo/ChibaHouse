package org.gragon.common.bus.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;

/**
 * bus 配置
 */
@AutoConfiguration
@RemoteApplicationEventScan(basePackages = "${spring.cloud.bus.base-packages}")
public class BusCustomConfiguration {

}
