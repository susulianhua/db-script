package org.tinygroup.flowbasiccomponent.util.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.FlowComponentConstants;
import org.tinygroup.flowbasiccomponent.util.CommEnumConverUntil;
import org.tinygroup.flowbasiccomponent.util.ResponseCodeConvertUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 枚举和响应码配置初始化组件
 *
 * @author qiucn
 */
public class InitializationPropertiesComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(InitializationPropertiesComponent.class);

    /**
     * 配置文件路径
     */
    private String filePath;
    /**
     * 枚举或者响应码。0-枚举；1-响应码
     */
    private int type;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "枚举和响应码配置初始化组件开始执行");
        switch (type) {
            case FlowComponentConstants.INITIALIZATION_PROPERTIES_TYPE_ENUM:
                CommEnumConverUntil.readFile(filePath);
                break;
            case FlowComponentConstants.INITIALIZATION_PROPERTIES_TYPE_RESPONSE_CODE:
                ResponseCodeConvertUtil.readFile(filePath);
                break;
            default:
                break;
        }
        LOGGER.logMessage(LogLevel.INFO, "枚举和响应码配置初始化组件执行结束");
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
