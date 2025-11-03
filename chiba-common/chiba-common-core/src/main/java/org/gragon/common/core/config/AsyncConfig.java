package org.gragon.common.core.config;

import cn.hutool.core.util.ArrayUtil;
import org.gragon.common.core.exception.ServiceException;
import org.gragon.common.core.utils.SpringUtils;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.Arrays;
import java.util.concurrent.Executor;

/**
 * 异步配置
 * 若为使用虚拟线程则生效
 */
public class AsyncConfig implements AsyncConfigurer {
    /**
     * 自定义 @Async 注解使用系统线程池
     * @return
     */
    public Executor getAsyncExecutor() {
        if(SpringUtils.isVirtual()) {
            return new VirtualThreadTaskExecutor("async-");
        }
        return SpringUtils.getBean("asyncExecutor");
    }

    /**
     * 异步执行异常处理
     * @return
     */
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            throwable.printStackTrace();
            StringBuilder sb = new StringBuilder();
            sb.append("Exception message - ").append(throwable.getMessage())
                .append(", Method name - ").append(method.getName());
            if (ArrayUtil.isNotEmpty(objects)) {
                sb.append(", Parameter value - ").append(Arrays.toString(objects));
            }
            throw new ServiceException(sb.toString());
        };
    }
}
