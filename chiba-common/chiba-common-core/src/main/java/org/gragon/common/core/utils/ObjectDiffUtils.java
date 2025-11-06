package org.gragon.common.core.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Field;

/**
 * 对象差异比较工具类
 */
public class ObjectDiffUtils {

    /**
     * 比较两个对象的差异，根据类型返回新值或旧值
     *
     * @param oldObj   旧对象
     * @param newObj   新对象
     * @param clazz    对象类型
     * @param diffType 差异类型：NEW_VALUES返回新值，OLD_VALUES返回旧值
     * @return 只包含变化字段的对象
     */
    public static <T> T getDiffObjects(T oldObj, T newObj, Class<T> clazz, DiffType diffType) {
        if (oldObj == null || newObj == null) {
            return null;
        }

        try {
            // 创建新实例
            T result = clazz.getDeclaredConstructor().newInstance();

            // 获取所有字段
            Field[] fields = ReflectUtil.getFields(clazz);

            for (Field field : fields) {
                String fieldName = field.getName();

                // 跳过序列化字段
                if ("serialVersionUID".equals(fieldName)) {
                    continue;
                }

                try {
                    field.setAccessible(true);
                    Object oldValue = field.get(oldObj);
                    Object newValue = field.get(newObj);

                    // 如果值有变化
                    if (!ObjectUtil.equal(oldValue, newValue)) {
                        // 根据类型设置新值或旧值
                        if (diffType == DiffType.NEW_VALUES) {
                            field.set(result, newValue);  // 返回新值
                        } else {
                            field.set(result, oldValue);  // 返回旧值
                        }
                    }
                    // 没变化的字段保持null

                } catch (IllegalAccessException e) {
                    // 忽略无法访问的字段
                }
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("创建差异对象失败", e);
        }
    }

    /**
     * 简化版本（默认返回新值）
     */
    @SuppressWarnings("unchecked")
    public static <T> T getDiffObjects(T oldObj, T newObj) {
        if (oldObj == null || newObj == null) {
            return null;
        }
        return (T) getDiffObjects(oldObj, newObj, (Class<T>) oldObj.getClass(), DiffType.NEW_VALUES);
    }

    /**
     * 简化版本（指定差异类型）
     */
    @SuppressWarnings("unchecked")
    public static <T> T getDiffObjects(T oldObj, T newObj, DiffType diffType) {
        if (oldObj == null || newObj == null) {
            return null;
        }
        return (T) getDiffObjects(oldObj, newObj, (Class<T>) oldObj.getClass(), diffType);
    }

    public static void main(String[] args) {
        class TestItem {
            private Long id;
            private String name;
            private Double price;
            private String description;

            public TestItem() {
            }

            public TestItem(Long id, String name, Double price, String description) {
                this.id = id;
                this.name = name;
                this.price = price;
                this.description = description;
            }

            // getter/setter
            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Double getPrice() {
                return price;
            }

            public void setPrice(Double price) {
                this.price = price;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            @Override
            public String toString() {
                return "TestItem{id=" + id + ", name='" + name + "', price=" + price + ", description='" + description + "'}";
            }
        }

        // 测试数据
        TestItem oldItem = new TestItem(1L, "商品A", 100.0, "原始描述");
        TestItem newItem = new TestItem(1L, "商品B", 150.0, "原始描述");

        System.out.println("旧对象: " + oldItem);
        System.out.println("新对象: " + newItem);

        // 测试返回新值
        TestItem newValues = getDiffObjects(oldItem, newItem, DiffType.NEW_VALUES);
        System.out.println("变化字段的新值: " + newValues);

        // 测试返回旧值
        TestItem oldValues = getDiffObjects(oldItem, newItem, DiffType.OLD_VALUES);
        System.out.println("变化字段的旧值: " + oldValues);

        // 简化版本（默认返回新值）
        TestItem defaultResult = getDiffObjects(oldItem, newItem);
        System.out.println("默认结果(新值): " + defaultResult);
    }

    /**
     * 差异类型枚举
     */
    public enum DiffType {
        NEW_VALUES,  // 返回变化字段的新值
        OLD_VALUES   // 返回变化字段的旧值
    }
}