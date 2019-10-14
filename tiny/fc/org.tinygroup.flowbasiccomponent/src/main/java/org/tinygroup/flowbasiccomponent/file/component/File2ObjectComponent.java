package org.tinygroup.flowbasiccomponent.file.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.FileUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 文件转对象
 *
 * @author qiucn
 */
public class File2ObjectComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(File2ObjectComponent.class);
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 转换对象的全路径
     */
    private String classPath;

    private String resultKey;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "文件转对象开始执行");
        context.put(resultKey, FileUtil.file2Object(filePath, classPath, null));
        LOGGER.logMessage(LogLevel.INFO, "文件转对象执行结束");
    }

}
