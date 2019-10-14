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
package org.tinygroup.lucene472.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.lucene472.LuceneConfigManager;
import org.tinygroup.lucene472.config.LuceneConfigs;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

/**
 * Lucene配置加载器
 *
 * @author yancheng11334
 */
public class LuceneConfigProcessor extends AbstractFileProcessor {

    private static final String LUCENE_CONFIG_EXTNAME = ".luceneconfig.xml";
    private static final String LUCENE_XSTREAM = "luceneconfig";

    private LuceneConfigManager luceneConfigManager;

    public LuceneConfigManager getLuceneConfigManager() {
        return luceneConfigManager;
    }

    public void setLuceneConfigManager(LuceneConfigManager luceneConfigManager) {
        this.luceneConfigManager = luceneConfigManager;
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(LUCENE_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除Lucene静态配置文件[{0}]",
                    fileObject.getAbsolutePath());
            LuceneConfigs luceneConfigs = (LuceneConfigs) caches.get(fileObject
                    .getAbsolutePath());
            if (luceneConfigs != null) {
                luceneConfigManager.removeLuceneConfigs(luceneConfigs);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除Lucene静态配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载Lucene静态配置文件[{0}]",
                    fileObject.getAbsolutePath());
            LuceneConfigs oldLuceneConfigs = (LuceneConfigs) caches.get(fileObject.getAbsolutePath());
            if (oldLuceneConfigs != null) {
                luceneConfigManager.removeLuceneConfigs(oldLuceneConfigs);
            }
            LuceneConfigs luceneConfigs = convertFromXml(stream, fileObject);
            luceneConfigManager.addLuceneConfigs(luceneConfigs);
            caches.put(fileObject.getAbsolutePath(), luceneConfigs);
            LOGGER.logMessage(LogLevel.INFO, "加载Lucene静态配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(LUCENE_CONFIG_EXTNAME);
    }

}
