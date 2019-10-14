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
import org.tinygroup.metadata.config.dict.Dicts;
import org.tinygroup.metadata.dict.DictProcessor;
import org.tinygroup.metadata.util.MetadataUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

/**
 * 数据字典文件扫描器
 * Created by wangwy11342 on 2017/5/19.
 */
public class DictFileProcessor extends AbstractFileProcessor {
    private DictProcessor dictProcessor;

    public void setDictProcessor(DictProcessor dictProcessor) {
        this.dictProcessor = dictProcessor;
    }

    @Override
    public void process() {
        XStream stream = XStreamFactory
                .getXStream(MetadataUtil.METADATA_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除dict文件[{0}]",
                    fileObject.getAbsolutePath());
            Dicts dicts = (Dicts) caches.get(fileObject.getAbsolutePath());
            if (dicts != null) {
                dictProcessor.removeDicts(dicts);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除dict文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载dict文件[{0}]",
                    fileObject.getAbsolutePath());
            Dicts oldDicts = (Dicts) caches.get(fileObject.getAbsolutePath());
            if (oldDicts != null) {
                dictProcessor.removeDicts(oldDicts);
            }
            Dicts dicts = convertFromXml(stream, fileObject);
            dictProcessor.addDicts(dicts);
            caches.put(fileObject.getAbsolutePath(), dicts);
            LOGGER.logMessage(LogLevel.INFO, "加载dict文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith("dict") || fileObject.getFileName().endsWith("dict.xml");
    }
}
