package org.tinygroup.flowbasiccomponent.test;

import org.tinygroup.flowbasiccomponent.CommEnumConverter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CommEnumConverterImpl<T> implements CommEnumConverter<T> {

    private static String TEST_COMMON_CONVERTER = "test_common_converter";
    @SuppressWarnings("rawtypes")
    private static Map<Class, Object> containsEnumMap = new HashMap<Class, Object>();

    static {
        containsEnumMap.put(DateWeekEnum.class, null);
    }

    @SuppressWarnings("unchecked")
    public T getEnum(String value, Class<T> t) {
        if (!t.isEnum()) {
            throw new RuntimeException("参数类型不是枚举类型");
        }
        Field[] fs = t.getFields();
        if (fs != null && fs.length > 0) {
            for (Field f : fs) {
                if (f.getName().equals(value)) {
                    try {
                        return (T) f.get(value);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getCause());
                    }
                }
            }
        }
        return null;
    }

    public boolean isMatch(Class<T> t) {
        if (containsEnumMap.containsKey(t)) {
            return true;
        }
        return false;
    }

    public String getType() {
        return TEST_COMMON_CONVERTER;
    }

}
