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
import org.tinygroup.lucene472.LuceneDynamicManager;
import org.tinygroup.lucene472.config.DynamicConfigs;
import org.tinygroup.lucene472.config.LuceneConfig;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

/**
 * 动态配置的lucene加载器
 *
 * @author yancheng11334
 */
public class LuceneDynamicProcessor extends AbstractFileProcessor {

    private static final String LUCENE_CONFIG_EXTNAME = ".lucenedynamic.xml";
    private static final String LUCENE_XSTREAM = "luceneconfig";

    private LuceneDynamicManager luceneDynamicManager;

    public LuceneDynamicManager getLuceneDynamicManager() {
        return luceneDynamicManager;
    }

    public void setLuceneDynamicManager(
            LuceneDynamicManager luceneDynamicManager) {
        this.luceneDynamicManager = luceneDynamicManager;
    }

    public void process() {
        XStream stream = XStreamFactory.getXStream(LUCENE_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除Lucene动态配置文件[{0}]",
                    fileObject.getAbsolutePath());
            DynamicConfigs dynamicConfigs = (DynamicConfigs) caches.get(fileObject
                    .getAbsolutePath());
            if (dynamicConfigs != null
                    && dynamicConfigs.getLuceneConfigList() != null) {
                for (LuceneConfig luceneConfig : dynamicConfigs
                        .getLuceneConfigList()) {
                    luceneDynamicManager.removeFullTextConfig(luceneConfig);
                }
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除Lucene动态配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }

        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载Lucene动态配置文件[{0}]",
                    fileObject.getAbsolutePath());
            DynamicConfigs oldDynamicConfigs = (DynamicConfigs) caches.get(fileObject.getAbsolutePath());
            if (oldDynamicConfigs != null && oldDynamicConfigs.getLuceneConfigList() != null) {
                for (LuceneConfig luceneConfig : oldDynamicConfigs
                        .getLuceneConfigList()) {
                    luceneDynamicManager.removeFullTextConfig(luceneConfig);
                }
            }
            DynamicConfigs dynamicConfigs = convertFromXml(stream, fileObject);
            if (dynamicConfigs != null
                    && dynamicConfigs.getLuceneConfigList() != null) {
                for (LuceneConfig luceneConfig : dynamicConfigs
                        .getLuceneConfigList()) {
                    luceneDynamicManager.addFullTextConfig(luceneConfig);
                }
            }
            caches.put(fileObject.getAbsolutePath(), dynamicConfigs);
            LOGGER.logMessage(LogLevel.INFO, "加载Lucene动态配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(LUCENE_CONFIG_EXTNAME);
    }

}
