package org.tinygroup.flowbasiccomponent;

import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.convert.objectxml.jaxb.ObjectToXml;
import org.tinygroup.convert.objectxml.jaxb.XmlToObject;
import org.tinygroup.flowbasiccomponent.errorcode.FlowComponentExceptionErrorCode;
import org.tinygroup.flowbasiccomponent.exception.FlowComponentException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.VFS;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class DefaultFileFormat implements FileFormat {
    public static String TINY_FILE_FORMAT_TYPE = "default_tiny_file_format_type";
    private static Logger LOGGER = LoggerFactory
            .getLogger(DefaultFileFormat.class);

    public static boolean isSimpleType(Class<?> clazz) {
        if (clazz.equals(int.class) || clazz.equals(char.class)
                || clazz.equals(byte.class) || clazz.equals(boolean.class)
                || clazz.equals(short.class) || clazz.equals(long.class)
                || clazz.equals(double.class) || clazz.equals(float.class)) {
            return true;
        }
        return clazz.equals(Integer.class) || clazz.equals(Character.class)
                || clazz.equals(Byte.class) || clazz.equals(Boolean.class)
                || clazz.equals(Short.class) || clazz.equals(Long.class)
                || clazz.equals(Double.class) || clazz.equals(Float.class)
                || clazz.equals(String.class);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object formatFile(String filePath, String classPath) {
        try {
            String xml = FileUtil.readFileContent(
                    new File(VFS.resolveFile(filePath).getAbsolutePath()),
                    getEncoding());
            Class<?> clazz = Class.forName(classPath);
            XmlToObject xmlToObject = new XmlToObject(clazz);
            return xmlToObject.convert(xml);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "对象：{0}转存到文件：{1}时出错。错误信息：{2}",
                    classPath, filePath, e);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.OBJECT_SAVE_TO_FILE_FAILED,
                    classPath, filePath, e);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> String formatObject(T t) {
        try {
            // 简单类型
            if (isSimpleType(t.getClass())) {
                return String.valueOf(t);
            }
            // 集合
            if (java.util.Collection.class.isAssignableFrom(t.getClass())) {
                Iterator iterator = ((Collection) t).iterator();
                String name = "";
                while (iterator.hasNext()) {
                    Class<?> clazz = iterator.next().getClass();
                    name = clazz.getName();
                    name = name.substring(name.lastIndexOf(".") + 1);
                    name = name.replaceFirst(name.substring(0, 1), name
                            .substring(0, 1).toLowerCase());
                    break;
                }
                org.tinygroup.convert.objectxml.simple.ObjectToXml objectToXml = new org.tinygroup.convert.objectxml.simple.ObjectToXml(
                        true, name + "-list", name);
                return (String) objectToXml.convert((List) t);
            }
            // 对象
            ObjectToXml objectToXml = new ObjectToXml(t.getClass(), true);
            return objectToXml.convert(t);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "对象:{0}转换为XML字符串失败,错误信息：{1}",
                    t.getClass().getName(), e);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.OBJECT_CONVERT_TO_XML_FAILED,
                    t.getClass().getName(), e);
        }
    }

    public String getType() {
        return TINY_FILE_FORMAT_TYPE;
    }

    public String getEncoding() {
        return "GB2312";
    }

}
