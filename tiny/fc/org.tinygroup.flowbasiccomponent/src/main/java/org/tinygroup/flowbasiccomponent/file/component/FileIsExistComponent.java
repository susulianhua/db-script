package org.tinygroup.flowbasiccomponent.file.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.FlowComponentConstants;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

/**
 * 文件是否存在判断
 *
 * @author qiucn
 */
public class FileIsExistComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FileIsExistComponent.class);
    /**
     * 文件路径
     */
    private String filePath;

    private String resultKey;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "文件存在判断组件开始执行");
        int code = FlowComponentConstants.DEFAULT_CODE;
        FileObject fileObject = VFS.resolveFile(filePath);
        if (fileObject.isExist()) {
            code = FlowComponentConstants.EXIST_CODE;
        }
        context.put(resultKey, code);
        LOGGER.logMessage(LogLevel.INFO, "文件存在判断组件执行结束");
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
