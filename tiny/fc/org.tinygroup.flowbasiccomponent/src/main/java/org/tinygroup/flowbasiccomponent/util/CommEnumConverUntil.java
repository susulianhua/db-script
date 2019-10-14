package org.tinygroup.flowbasiccomponent.util;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.flowbasiccomponent.CommEnumConverter;
import org.tinygroup.flowbasiccomponent.FlowComponentConstants;
import org.tinygroup.flowbasiccomponent.errorcode.FlowComponentExceptionErrorCode;
import org.tinygroup.flowbasiccomponent.exception.FlowComponentException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.VFS;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

@SuppressWarnings("rawtypes")
public class CommEnumConverUntil {
    private static Logger LOGGER = LoggerFactory
            .getLogger(CommEnumConverUntil.class);
    private static Map<String, CommEnumConverter> commEnumConverts = new HashMap<String, CommEnumConverter>();
    private static Map<String, String> enumMap = new HashMap<String, String>();

    static {
        Collection<CommEnumConverter> collection = BeanContainerFactory
                .getBeanContainer(CommEnumConverUntil.class.getClassLoader())
                .getBeans(CommEnumConverter.class);
        for (CommEnumConverter convert : collection) {
            commEnumConverts.put(convert.getType(), convert);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getEnum(String classPath, String value, String type) {
        CommEnumConverter converter = commEnumConverts.get(type);
        if (converter == null) {
            LOGGER.logMessage(LogLevel.ERROR, "找不到指定的枚举转换器：{0}", type);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.ENUM_CONVERTER_NOT_FOUNT,
                    type);
        }
        Class clazz = null;
        try {
            clazz = Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            LOGGER.logMessage(LogLevel.ERROR, "找不到指定的枚举类：{0}", classPath);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.ENUM_NOT_FOUND, classPath);
        }
        if (!converter.isMatch(clazz)) {
            LOGGER.logMessage(LogLevel.ERROR, "指定的枚举转换类：{0}不支持枚举：{1}的转换",
                    converter.getClass().getName(), classPath);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.ENUM_CONVERTER_NOT_SUPPORT,
                    converter.getClass().getName(), classPath);
        }
        return (T) converter.getEnum(value, clazz);
    }

    public static void readFile(String filePath) {
        Properties prop = new Properties();
        InputStream in = VFS.resolveFile(filePath).getInputStream();
        try {
            prop.load(in);
        } catch (IOException e) {
            LOGGER.logMessage(LogLevel.ERROR, "properties文件：{0}读取失败，错误信息：{1}",
                    filePath, e);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.PROPERTIES_FILE_READ_FAILED,
                    filePath, e);
        }
        // 返回Properties中包含的key-value的Set视图
        Set<Entry<Object, Object>> set = prop.entrySet();
        // 返回在此Set中的元素上进行迭代的迭代器
        Iterator<Map.Entry<Object, Object>> it = set.iterator();
        String key = null, value = null;
        // 循环取出key-value
        while (it.hasNext()) {
            Entry<Object, Object> entry = it.next();
            key = String.valueOf(entry.getKey());
            value = String.valueOf(entry.getValue());
            key = key == null ? key : key.trim();
            value = value == null ? value : value.trim();
            // 将key-value放入map中
            enumMap.put(key, value);
        }
    }

    public static String codeConvert(String key) {
        if (enumMap.containsKey(key)) {
            return enumMap.get(key);
        }
        return enumMap.get(FlowComponentConstants.TINY_ENUM_DEFAULT_CODE);
    }
}
