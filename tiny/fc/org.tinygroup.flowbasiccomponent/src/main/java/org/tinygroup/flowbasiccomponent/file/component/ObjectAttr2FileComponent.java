package org.tinygroup.flowbasiccomponent.file.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flow.util.FlowElUtil;
import org.tinygroup.flowbasiccomponent.util.FileUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 对象属性转文件
 *
 * @author qiucn
 */
public class ObjectAttr2FileComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ObjectAttr2FileComponent.class);
    /**
     * 获取属性值的el表达式
     */
    private String el;

    private String filePath;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "对象属性转文件开始执行");
        FileUtil.object2File(filePath, FlowElUtil.execute(el, context, this
                .getClass().getClassLoader()), null);
        LOGGER.logMessage(LogLevel.INFO, "对象属性转文件执行结束");
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getEl() {
        return el;
    }

    public void setEl(String el) {
        this.el = el;
    }
}
