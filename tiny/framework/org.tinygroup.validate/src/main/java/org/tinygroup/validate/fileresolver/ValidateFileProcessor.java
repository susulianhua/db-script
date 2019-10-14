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
import org.tinygroup.validate.XmlValidatorManager;
import org.tinygroup.validate.config.ObjectValidators;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import java.io.InputStream;

/**
 * xml验证文件搜索器
 *
 * @author renhui
 */
public class ValidateFileProcessor extends AbstractFileProcessor {

    private static final String VALIDATE_FILE_SUFFIX = ".validate.xml";
    XmlValidatorManager validatorManager;

    public XmlValidatorManager getValidatorManager() {
        return validatorManager;
    }

    public void setValidatorManager(XmlValidatorManager validatorManager) {
        this.validatorManager = validatorManager;
    }

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(VALIDATE_FILE_SUFFIX);
    }

    public void process() {
//		 = SpringBeanContainer
//				.getBean(XmlValidatorManager.VALIDATOR_MANAGER_BEAN_NAME);
        XStream stream = XStreamFactory
                .getXStream(ValidatorManager.XSTEAM_PACKAGE_NAME);

        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除xml校验配置文件[{0}]",
                    fileObject.getAbsolutePath());
            ObjectValidators objectValidatorConfigs = (ObjectValidators) caches.get(fileObject
                    .getAbsolutePath());
            if (objectValidatorConfigs != null) {
                validatorManager.removeObjectValidatorConfigs(objectValidatorConfigs);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除const文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }

        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.DEBUG, "正在加载xml校验配置文件[{0}]",
                    fileObject.getAbsolutePath());
            ObjectValidators oldObjectValidatorConfigs = (ObjectValidators) caches.get(fileObject
                    .getAbsolutePath());
            if (oldObjectValidatorConfigs != null) {
                validatorManager.removeObjectValidatorConfigs(oldObjectValidatorConfigs);
            }
            InputStream inputStream = fileObject.getInputStream();
            ObjectValidators objectValidatorConfigs = (ObjectValidators) stream
                    .fromXML(inputStream);
            try {
                inputStream.close();
            } catch (Exception e) {
                LOGGER.errorMessage("关闭文件流时出错,文件路径:{}", e, fileObject.getAbsolutePath());
            }
            validatorManager
                    .addObjectValidatorConfigs(objectValidatorConfigs);
            caches.put(fileObject.getAbsolutePath(), objectValidatorConfigs);
            LOGGER.logMessage(LogLevel.DEBUG, "加载xml校验配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }

    }

}
