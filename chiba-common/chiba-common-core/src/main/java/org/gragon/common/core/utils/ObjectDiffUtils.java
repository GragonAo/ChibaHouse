package org.gragon.common.core.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * 对象差异比较工具类（支持复杂对象、集合、Map、LocalDateTime）
 */
public class ObjectDiffUtils {

    /**
     * 比较两个对象的差异，返回只包含变化字段的新旧值
     *
     * @param oldObj 旧对象
     * @param newObj 新对象
     * @param clazz  对象类型
     * @return 包含差异字段的 DiffNode（未变化的字段为 null）
     */
    public static <T> DiffNode<T> getDiffObjects(T oldObj, T newObj, Class<T> clazz) {
        if (oldObj == null || newObj == null) {
            return null;
        }

        try {
            DiffNode<T> diffNode = new DiffNode<>();
            diffNode.oldObj = clazz.getDeclaredConstructor().newInstance();
            diffNode.newObj = clazz.getDeclaredConstructor().newInstance();

            Field[] fields = ReflectUtil.getFields(clazz);

            for (Field field : fields) {
                if ("serialVersionUID".equals(field.getName())) {
                    continue;
                }

                field.setAccessible(true);
                Object oldValue = field.get(oldObj);
                Object newValue = field.get(newObj);

//                if (newValue != null && !deepEquals(oldValue, newValue)) {
                if (newValue != null && !ObjectUtil.equals(newValue, oldValue)) {
                    field.set(diffNode.oldObj, oldValue);
                    field.set(diffNode.newObj, newValue);
                }
            }

            return diffNode;
        } catch (Exception e) {
            throw new RuntimeException("创建差异对象失败", e);
        }
    }
    
    @Data
    public static class DiffNode<T> {
        T oldObj;
        T newObj;
    }
}
