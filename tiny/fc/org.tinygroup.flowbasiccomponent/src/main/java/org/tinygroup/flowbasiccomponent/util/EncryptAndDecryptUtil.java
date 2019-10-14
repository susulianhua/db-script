package org.tinygroup.flowbasiccomponent.util;

import org.tinygroup.commons.cryptor.Cryptor;
import org.tinygroup.commons.cryptor.DefaultCryptor;
import org.tinygroup.flowbasiccomponent.errorcode.FlowComponentExceptionErrorCode;
import org.tinygroup.flowbasiccomponent.exception.FlowComponentException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 加解密工具类
 *
 * @author qiucn
 */
public class EncryptAndDecryptUtil {
    private static Logger LOGGER = LoggerFactory
            .getLogger(EncryptAndDecryptUtil.class);

    public static String encrypt(String plainKey) {
        Cryptor cryptor = new DefaultCryptor();
        try {
            return cryptor.encrypt(plainKey);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "加密失败，错误信息如下：{0}",
                    e);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.ENCRYPT_FAILED, e);
        }
    }

    public static String decrypt(String encryptedKey) {
        Cryptor cryptor = new DefaultCryptor();
        try {
            return cryptor.decrypt(encryptedKey);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "解密失败，错误信息如下：{0}",
                    e);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.DECRYPT_FAILED, e);
        }
    }

}
