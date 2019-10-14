package org.tinygroup.flowbasiccomponent;

public interface CommEnumConverter<T> {

    /**
     * 根据枚举类型，将传入的值转换为对应的枚举对象
     *
     * @param b 待转换的值
     * @param t 目标枚举类型
     * @return 枚举实例
     */
    T getEnum(String value, Class<T> t);

    /**
     * 根据传入的枚举类型，判断本处理器是否能对其进行处理，如果可以，则返回true
     *
     * @param t
     * @return
     */
    boolean isMatch(Class<T> t);

    /**
     * m枚举转换器类型标识
     *
     * @return
     */
    String getType();
}
