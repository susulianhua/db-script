package org.tinygroup.flowbasiccomponent.file.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.FileUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 文件大小获取组件
 *
 * @author qiucn
 */
public class GetFileSizeComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(GetFileSizeComponent.class);
    /**
     * 文件路径
     */
    private String filePath;

    private String resultKey;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "文件大小获取组件开始执行");
        context.put(resultKey, FileUtil.getFileSize(filePath));
        LOGGER.logMessage(LogLevel.INFO, "文件大小获取组件执行结束");
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }
}
