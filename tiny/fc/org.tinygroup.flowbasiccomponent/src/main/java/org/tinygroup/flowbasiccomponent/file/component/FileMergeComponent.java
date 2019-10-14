package org.tinygroup.flowbasiccomponent.file.component;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.FileUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件合并,将文件1和文件2合并到文件3中，如果文件3路径为空，则将文件2合并到文件1
 *
 * @author qiucn
 */
public class FileMergeComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FileMergeComponent.class);
    /**
     * 文件1
     */
    private String filePath1;
    /**
     * 文件2
     */
    private String filePath2;
    /**
     * 文件3
     */
    private String filePath3;

    public String getFilePath1() {
        return filePath1;
    }

    public void setFilePath1(String filePath1) {
        this.filePath1 = filePath1;
    }

    public String getFilePath2() {
        return filePath2;
    }

    public void setFilePath2(String filePath2) {
        this.filePath2 = filePath2;
    }

    public String getFilePath3() {
        return filePath3;
    }

    public void setFilePath3(String filePath3) {
        this.filePath3 = filePath3;
    }

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "文件合并组件开始执行");
        List<String> files = new ArrayList<String>();
        if (StringUtil.isBlank(filePath3)) {
            files.add(filePath2);
            FileUtil.mergeFiles(filePath1, files);
        } else {
            files.add(filePath1);
            files.add(filePath2);
            FileUtil.mergeFiles(filePath3, files);
        }
        LOGGER.logMessage(LogLevel.INFO, "文件合并组件执行结束");
    }

}
