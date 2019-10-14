package org.tinygroup.flowbasiccomponent.util;

import org.tinygroup.flowbasiccomponent.errorcode.FlowComponentExceptionErrorCode;
import org.tinygroup.flowbasiccomponent.exception.FlowComponentException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class IniOperatorUtil {
    private static Logger LOGGER = LoggerFactory
            .getLogger(IniOperatorUtil.class);

    /**
     * 从ini配置文档中读取变量的值
     *
     * @param file     配置文档的路径
     * @param section  要获取的变量所在段名称
     * @param variable 要获取的变量名称
     * @return 变量的值
     */
    public static String getProperty(String filePath, String section,
                                     String variable) {
        return readFile(filePath, section, variable);
    }

    private static String readFile(String filePath, String section,
                                   String variable) {
        try {
            FileObject fileObject = VFS.resolveFile(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    fileObject.getInputStream(), "UTF-8"));
            String line = null;
            String value = "";
            boolean isSection = false;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // 此部分为注释,不做操作
                if (line.matches("^\\#.*$")) {

                } else if (line.matches("^\\[\\S+\\]$")) {
                    // section
                    String currentSection = line.replaceFirst("^\\[(\\S+)\\]$",
                            "$1");
                    if (section.equals(currentSection)) {
                        isSection = true;
                    }
                } else if (line.matches("^\\S+=.*$")) {
                    // key,value
                    if (isSection) {
                        int i = line.indexOf("=");
                        String key = line.substring(0, i).trim();
                        if (variable.equals(key)) {
                            value = line.substring(i + 1).trim();
                        }
                    }
                }
            }
            return value;
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR,
                    "ini文件：{0}读取属性域：{1}的属性：{2}时失败，失败原因：{3}", filePath, section,
                    variable, e);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.INI_PROPERTY_READ_FAILED,
                    filePath, section, variable, e);
        }
    }

}
