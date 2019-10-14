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
package org.tinygroup.aopcache.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.aopcache.AopCacheConfigManager;
import org.tinygroup.aopcache.config.AopCaches;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

/**
 * @author renhui
 */
public class CacheActionFileProcessor extends AbstractFileProcessor {

    private static final String AOP_CACHE_EXT_FILENAME = ".aopcache.xml";
    private AopCacheConfigManager manager;


    public void setManager(AopCacheConfigManager manager) {
        this.manager = manager;
    }

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(AOP_CACHE_EXT_FILENAME);
    }

    public void process() {

        XStream stream = XStreamFactory
                .getXStream(AopCacheConfigManager.XSTEAM_PACKAGE_NAME);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除aop缓存配置文件[{0}]",
                    fileObject.getAbsolutePath());
            AopCaches aopCaches = (AopCaches) caches.get(fileObject
                    .getAbsolutePath());
            if (aopCaches != null) {
                manager.removeAopCaches(aopCaches);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除aop缓存配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载aop缓存配置文件[{0}]",
                    fileObject.getAbsolutePath());
            AopCaches oldMatchers = (AopCaches) caches.get(fileObject
                    .getAbsolutePath());
            if (oldMatchers != null) {
                manager.removeAopCaches(oldMatchers);
            }
            AopCaches matchers = convertFromXml(stream, fileObject);
            manager.addAopCaches(matchers);
            caches.put(fileObject.getAbsolutePath(), matchers);
            LOGGER.logMessage(LogLevel.INFO, "加载aop缓存配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }

    }

}
