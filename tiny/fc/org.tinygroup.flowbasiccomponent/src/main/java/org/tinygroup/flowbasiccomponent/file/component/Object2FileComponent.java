package org.tinygroup.flowbasiccomponent.file.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.FileUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 对象转文件
 *
 * @author qiucn
 */
public class Object2FileComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(Object2FileComponent.class);
    /**
     * 文件在上下文存放的key
     */
    private String key;

    private String filePath;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "对象转文件开始执行");
        FileUtil.object2File(filePath, context.get(key), null);
        LOGGER.logMessage(LogLevel.INFO, "对象转文件执行结束");
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
