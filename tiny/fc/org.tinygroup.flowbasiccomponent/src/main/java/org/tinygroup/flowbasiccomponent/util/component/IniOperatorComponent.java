package org.tinygroup.flowbasiccomponent.util.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.IniOperatorUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 获取ini文件属性
 *
 * @author qiucn
 */
public class IniOperatorComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(IniOperatorComponent.class);
    /**
     * ini文件 路径
     */
    private String filePath;
    /**
     * 要获取的变量所在段名称
     */
    private String section;

    /**
     * 要获取的变量名称
     */
    private String variable;

    /**
     * 结果集在上下文存放的key
     */
    private String resultKey;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "ini文件属性获取组件开始执行");
        context.put(resultKey,
                IniOperatorUtil.getProperty(filePath, section, variable));
        LOGGER.logMessage(LogLevel.INFO, "ini文件属性获取组件执行结束");
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }
}
