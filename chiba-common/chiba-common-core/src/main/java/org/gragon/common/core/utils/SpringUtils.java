package org.gragon.common.core.utils;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.thread.Threading;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * spring工具类
 */
public final class SpringUtils extends SpringUtil {

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     * @param name bean名称
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
     * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     * @param name bean名称
     * @return boolean
     */
    public static boolean isSingleton(String name) {
        return getBeanFactory().isSingleton(name);
    }

    /**
     * Class 注册对象的类型
     * @param name bean名称
     * @return Class<?>
     */
    public static Class<?> getType(String name)throws NoSuchBeanDefinitionException {
        return getBeanFactory().getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     * @param name bean名称
     * @return String[]
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().getAliases(name);
    }

    /**
     * 获取aop代理对象
     * @param invoker 代理对象
     * @return T
     */
    public static <T> T getAopProxy(T invoker) {
        return (T) getBean(invoker.getClass());
    }

    /**
     * 获取spring上下文
     * @return ApplicationContext
     */
    public static ApplicationContext context() {
        return getApplicationContext();
    }

    /**
     * 判断是否为虚拟机
     * @return boolean
     */
    public static boolean isVirtual() {
        return Threading.VIRTUAL.isActive(getBean(Environment.class));
    }
}
