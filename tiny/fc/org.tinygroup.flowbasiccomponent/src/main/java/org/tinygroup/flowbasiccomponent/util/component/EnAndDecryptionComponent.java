package org.tinygroup.flowbasiccomponent.util.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.FlowComponentConstants;
import org.tinygroup.flowbasiccomponent.util.EncryptAndDecryptUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 加解密组件
 *
 * @author qiucn
 */
public class EnAndDecryptionComponent implements ComponentInterface {

    private Logger LOGGER = LoggerFactory
            .getLogger(EnAndDecryptionComponent.class);

    /**
     * 需要加解密的报文
     */
    private String message;

    /**
     * 加密或者解密标识
     */
    private Integer type = 1;

    /**
     * 加解密结果
     */
    private String resultKey;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "加解密组件开始执行");
        if (type == FlowComponentConstants.ENCRYPT_TYPE) {
            context.put(resultKey, EncryptAndDecryptUtil.encrypt(message));
        } else if (type == FlowComponentConstants.DECRYPT_TYPE) {
            context.put(resultKey, EncryptAndDecryptUtil.decrypt(message));
        }
        LOGGER.logMessage(LogLevel.INFO, "加解密组件执行结束");
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
