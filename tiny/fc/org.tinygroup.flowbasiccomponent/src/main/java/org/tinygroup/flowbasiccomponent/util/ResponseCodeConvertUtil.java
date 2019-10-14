package org.tinygroup.flowbasiccomponent.util;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.flowbasiccomponent.CodeConvert;
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

/**
 * 根据输入信息转换成另外的编码信息，例如输入0001输出9001 转换规则需要通过实现接口CodeConvert
 *
 * @author qiucn
 */
public class ResponseCodeConvertUtil {
    private static Logger LOGGER = LoggerFactory
            .getLogger(ResponseCodeConvertUtil.class);

    private static Map<String, CodeConvert> codeConverts = new HashMap<String, CodeConvert>();
    private static Map<String, String> responseCodeMap = new HashMap<String, String>();

    static {
        Collection<CodeConvert> collection = BeanContainerFactory
                .getBeanContainer(
                        ResponseCodeConvertUtil.class.getClassLoader())
                .getBeans(CodeConvert.class);
        for (CodeConvert convert : collection) {
            codeConverts.put(convert.getType(), convert);
        }
    }

    public static String covert(String type, String source) {
        CodeConvert convert = codeConverts.get(type);
        if (convert == null) {
            LOGGER.logMessage(LogLevel.ERROR, "响应码类型：{0}对应的转换器未配置",
                    type);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.RESPCODE_CONVERT_NOT_FOUND,
                    type);
        }
        return convert.convert(source);
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
            responseCodeMap.put(key, value);
        }
    }

    public static String codeConvert(String key) {
        if (responseCodeMap.containsKey(key)) {
            return responseCodeMap.get(key);
        }
        return responseCodeMap.get(FlowComponentConstants.TINY_RESPONSE_DEFAULT_CODE);
    }
}
