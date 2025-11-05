package org.gragon.storage;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 收纳模块
 *
 * @author Chiba
 */
@EnableDubbo
@SpringBootApplication
public class ChibaStorageApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ChibaStorageApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  物品收纳模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
