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
package org.tinygroup.metadata.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.metadata.config.errormessage.ErrorMessages;
import org.tinygroup.metadata.errormessage.ErrorMessageProcessor;
import org.tinygroup.metadata.exception.MetadataRuntimeException;
import org.tinygroup.metadata.util.MetadataUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

public class ErrorMessageFileResolver extends AbstractFileProcessor {

    private static final String ERROR_EXTFILENAME = ".error.xml";
    private ErrorMessageProcessor errorMessageProcessor;

    public ErrorMessageProcessor getErrorMessageProcessor() {
        return errorMessageProcessor;
    }

    public void setErrorMessageProcessor(ErrorMessageProcessor errorMessageProcessor) {
        this.errorMessageProcessor = errorMessageProcessor;
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(MetadataUtil.METADATA_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除error文件[{0}]",
                    fileObject.getAbsolutePath());
            ErrorMessages errorMessages = (ErrorMessages) caches.get(fileObject
                    .getAbsolutePath());
            if (errorMessages != null) {
                errorMessageProcessor.removeErrorMessages(errorMessages);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除error文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载error文件[{0}]",
                    fileObject.getAbsolutePath());
            ErrorMessages oldErrorMessages = (ErrorMessages) caches.get(fileObject.getAbsolutePath());
            if (oldErrorMessages != null) {
                errorMessageProcessor.removeErrorMessages(oldErrorMessages);
            }
            ErrorMessages errorMessages = convertFromXml(stream, fileObject);
            try {
                errorMessageProcessor.addErrorMessages(errorMessages);
            } catch (MetadataRuntimeException e) {
                String errorMsg = e.getMessage();
                if (e.getMessage() != null) {
                    errorMsg += ",file:[" + fileObject.getAbsolutePath() + "]";
                }
                LOGGER.logMessage(LogLevel.ERROR, errorMsg, e);
            }
            caches.put(fileObject.getAbsolutePath(), errorMessages);
            LOGGER.logMessage(LogLevel.INFO, "加载error文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(ERROR_EXTFILENAME) || fileObject.getFileName().endsWith(".error");
    }

}
