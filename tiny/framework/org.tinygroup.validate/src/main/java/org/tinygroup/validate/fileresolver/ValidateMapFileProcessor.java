/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.validate.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.validate.ValidatorManager;
import org.tinygroup.validate.ValidatorMapStorage;
import org.tinygroup.validate.config.Validators;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import java.io.InputStream;

/**
 * 验证映射文件搜索器
 *
 * @author renhui
 */
public class ValidateMapFileProcessor extends AbstractFileProcessor {

    private static final String VALIDATE_MAP_FILE_SUFFIX = ".validatemap.xml";
    ValidatorMapStorage validatorManager;


    public ValidatorMapStorage getValidatorManager() {
        return validatorManager;
    }

    public void setValidatorManager(ValidatorMapStorage validatorManager) {
        this.validatorManager = validatorManager;
    }

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(VALIDATE_MAP_FILE_SUFFIX);
    }

    public void process() {
//		ValidatorMapStorage validatorManager = SpringBeanContainer
//				.getBean(ValidatorMapStorage.VALIDATOR_MAP_BEAN_NAME);

        XStream stream = XStreamFactory
                .getXStream(ValidatorManager.XSTEAM_PACKAGE_NAME);

        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除校验映射配置文件[{0}]",
                    fileObject.getAbsolutePath());
            Validators validators = (Validators) caches.get(fileObject
                    .getAbsolutePath());
            if (validators != null) {
                validatorManager.removeValidators(validators);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除校验映射配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }

        for (FileObject fileObject : changeList) {

            LOGGER.logMessage(LogLevel.INFO, "正在加载校验映射配置文件[{0}]",
                    fileObject.getAbsolutePath());
            Validators oldValidators = (Validators) caches.get(fileObject
                    .getAbsolutePath());
            if (oldValidators != null) {
                validatorManager.removeValidators(oldValidators);
            }
            InputStream inputStream = fileObject
                    .getInputStream();
            Validators validators = (Validators) stream.fromXML(inputStream);
            try {
                inputStream.close();
            } catch (Exception e) {
                LOGGER.errorMessage("关闭文件流时出错,文件路径:{}", e, fileObject.getAbsolutePath());
            }
            validatorManager.addValidators(validators);
            caches.put(fileObject.getAbsolutePath(), validators);

            LOGGER.logMessage(LogLevel.INFO, "加载校验映射配置文件[{0}]结束",
                    fileObject.getAbsolutePath());

        }

    }

}
