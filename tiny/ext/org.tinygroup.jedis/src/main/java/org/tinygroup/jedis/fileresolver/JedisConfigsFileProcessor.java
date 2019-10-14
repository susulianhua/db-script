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
package org.tinygroup.jedis.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.jedis.JedisManager;
import org.tinygroup.jedis.config.JedisConfigs;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import java.io.InputStream;

/**
 * Jedis配置文件处理器，加载Jedis客户端配置信息
 *
 * @author yancheng11334
 */
public class JedisConfigsFileProcessor extends AbstractFileProcessor {

    private static final String JEDIS_CONFIG_EXT_NAME = ".jedisconfig.xml";

    private static final String JEDIS_XSTREAM_NAME = "jedis";

    private JedisManager jedisManager;

    public JedisManager getJedisManager() {
        return jedisManager;
    }

    public void setJedisManager(JedisManager jedisManager) {
        this.jedisManager = jedisManager;
    }

    public void process() {
        XStream stream = XStreamFactory.getXStream(JEDIS_XSTREAM_NAME);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除Redis配置文件[{0}]",
                    fileObject.getAbsolutePath());
            JedisConfigs jedisConfigs = (JedisConfigs) caches.get(fileObject
                    .getAbsolutePath());
            if (jedisConfigs != null) {
                jedisManager.removeJedisConfigs(jedisConfigs);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除Redis配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载Redis配置文件[{0}]",
                    fileObject.getAbsolutePath());
            JedisConfigs oldJedisConfigs = (JedisConfigs) caches.get(fileObject
                    .getAbsolutePath());
            if (oldJedisConfigs != null) {
                jedisManager.removeJedisConfigs(oldJedisConfigs);
            }
            InputStream inputStream = fileObject.getInputStream();
            JedisConfigs jedisConfigs = (JedisConfigs) stream
                    .fromXML(inputStream);
            try {
                inputStream.close();
            } catch (Exception e) {
                LOGGER.errorMessage("关闭文件流时出错,文件路径:{}", e, fileObject.getAbsolutePath());
            }
            jedisManager.addJedisConfigs(jedisConfigs);
            caches.put(fileObject.getAbsolutePath(), jedisConfigs);
            LOGGER.logMessage(LogLevel.INFO, "加载Redis配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(JEDIS_CONFIG_EXT_NAME);
    }

}
