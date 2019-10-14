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
package org.tinygroup.trans.template.fileprocessor;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.trans.template.TemplateConverts;
import org.tinygroup.trans.template.manager.TemplateConvertManager;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import java.io.InputStream;

public class TemplateConvertFileProcessor extends AbstractFileProcessor {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(TemplateConvertFileProcessor.class);
    private static final String TEMPLATE_CONVERT_FILENAME = ".templateconvert.xml";
    private static final String TEMPLATE_CONVERT_XSTREAM_PACKAGENAME = "template-convert";

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(TEMPLATE_CONVERT_FILENAME);
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(TemplateConvertFileProcessor.TEMPLATE_CONVERT_XSTREAM_PACKAGENAME);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除templateconvert文件[{0}]",
                    fileObject.getAbsolutePath());
            TemplateConverts info = (TemplateConverts) caches.get(fileObject
                    .getAbsolutePath());
            if (info != null) {
                caches.remove(fileObject.getAbsolutePath());
                TemplateConvertManager.remove(info);
            }
            LOGGER.logMessage(LogLevel.INFO, "移除templateconvert文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在读取templateconvert文件[{0}]",
                    fileObject.getAbsolutePath());
            TemplateConverts oldConverts = (TemplateConverts) caches
                    .get(fileObject.getAbsolutePath());
            if (oldConverts != null) {
                caches.remove(fileObject.getAbsolutePath());
                TemplateConvertManager.remove(oldConverts);
            }
            InputStream inputStream = fileObject.getInputStream();
            TemplateConverts converts = (TemplateConverts) stream
                    .fromXML(inputStream);
            try {
                inputStream.close();
            } catch (Exception e) {
                LOGGER.errorMessage("关闭文件流时出错,文件路径:{}", e,
                        fileObject.getAbsolutePath());
            }
            caches.put(fileObject.getAbsolutePath(), converts);
            TemplateConvertManager.put(converts);
            LOGGER.logMessage(LogLevel.INFO, "读取templateconvert文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

}
