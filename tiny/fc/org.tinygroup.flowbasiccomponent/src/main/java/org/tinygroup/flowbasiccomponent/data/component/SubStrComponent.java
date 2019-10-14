package org.tinygroup.flowbasiccomponent.data.component;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.errorcode.FlowComponentExceptionErrorCode;
import org.tinygroup.flowbasiccomponent.exception.FlowComponentException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 数据元素处理，字符串剪切
 *
 * @author qiucn
 */
public class SubStrComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SubStrComponent.class);
    /**
     * 剪切前字符串
     */
    private String preStr;
    /**
     * 剪切开始位置
     */
    private int beginIndex;
    /**
     * 剪切结束位置
     */
    private int endIndex;

    private String resultKey;

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public String getPreStr() {
        return preStr;
    }

    public void setPreStr(String preStr) {
        this.preStr = preStr;
    }

    public void execute(Context context) {
        if (StringUtil.isBlank(preStr)) {
            LOGGER.logMessage(LogLevel.ERROR, "需要剪切的字符串为空");
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.FLOW_PARAMETER_NULL,
                    "preStr");
        }
        LOGGER.logMessage(LogLevel.INFO, "字符串剪切组件开始执行");
        context.put(resultKey, preStr.substring(beginIndex, endIndex));
        LOGGER.logMessage(LogLevel.INFO, "字符串剪切组件执行结束");
    }

}
