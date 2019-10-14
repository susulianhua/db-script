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
package org.tinygroup.stopword.fileresolver;

import org.apache.lucene.analysis.util.WordlistLoader;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.stopword.StopWordManager;
import org.tinygroup.vfs.FileObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 停止词加载器
 * @author yancheng11334
 *
 */
public class StopWordFileProcessor extends AbstractFileProcessor {

    private StopWordManager stopWordManager;

    public StopWordManager getStopWordManager() {
        return stopWordManager;
    }

    public void setStopWordManager(StopWordManager stopWordManager) {
        this.stopWordManager = stopWordManager;
    }

    public void process() {
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.DEBUG, "正在移除停止词文件[{0}]",
                    fileObject.getAbsolutePath());
            List<String> stopWords = (List<String>) caches.get(fileObject.getAbsolutePath());
            if (stopWords != null) {
                stopWordManager.removeStopWords(stopWords);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.DEBUG, "移除停止词文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }

        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.DEBUG, "正在加载停止词文件[{0}]",
                    fileObject.getAbsolutePath());
            List<String> oldStopWords = (List<String>) caches.get(fileObject.getAbsolutePath());
            if (oldStopWords != null) {
                stopWordManager.removeStopWords(oldStopWords);
            }
            List<String> stopWords = readStopWords(fileObject);
            if (stopWords != null) {
                stopWordManager.addStopWords(stopWords);
                caches.put(fileObject.getAbsolutePath(), stopWords);
            }
            LOGGER.logMessage(LogLevel.DEBUG, "加载停止词文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    private List<String> readStopWords(FileObject fileObject) {
        List<String> stopWords = new ArrayList<String>();
        InputStream inputStream = fileObject.getInputStream();
        try {
            stopWords = WordlistLoader.getLines(inputStream, Charset.forName("utf-8"));
        } catch (IOException e) {
            LOGGER.errorMessage("读取停止词文件[{0}]发生异常", e, fileObject.getAbsolutePath());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.errorMessage("关闭停止词文件[{0}]发生异常", e, fileObject.getAbsolutePath());
                }
            }
        }
        return stopWords;
    }

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(".stopdic");
    }

}
