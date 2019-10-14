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
import org.tinygroup.metadata.config.defaultvalue.DefaultValues;
import org.tinygroup.metadata.defaultvalue.DefaultValueProcessor;
import org.tinygroup.metadata.util.MetadataUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

/**
 * Created by wangwy11342 on 2016/7/16.
 */
public class DefaultValueFileResolver extends AbstractFileProcessor {
    private static final String DEFAULTVALUE_EXTFILENAME = ".defaultvalue.xml";

    private DefaultValueProcessor defaultValueProcessor;

    public DefaultValueProcessor getDefaultValueProcessor() {
        return defaultValueProcessor;
    }

    public void setDefaultValueProcessor(DefaultValueProcessor defaultValueProcessor) {
        this.defaultValueProcessor = defaultValueProcessor;
    }

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(DEFAULTVALUE_EXTFILENAME) || fileObject.getFileName().endsWith(".defaultvalue");
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(MetadataUtil.METADATA_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除default文件[{0}]",
                    fileObject.getAbsolutePath());
            DefaultValues defaultValues = (DefaultValues) caches.get(fileObject.getAbsolutePath());
            if (defaultValues != null) {
                defaultValueProcessor.removeDefaultValues(defaultValues);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除default文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载default文件[{0}]",
                    fileObject.getAbsolutePath());
            DefaultValues oldDefaultValues = (DefaultValues) caches.get(fileObject.getAbsolutePath());
            if (oldDefaultValues != null) {
                defaultValueProcessor.removeDefaultValues(oldDefaultValues);
            }
            DefaultValues defaultValues = convertFromXml(stream, fileObject);
            defaultValueProcessor.addDefaultValues(defaultValues);
            caches.put(fileObject.getAbsolutePath(), defaultValues);
            LOGGER.logMessage(LogLevel.INFO, "加载default文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }
}
