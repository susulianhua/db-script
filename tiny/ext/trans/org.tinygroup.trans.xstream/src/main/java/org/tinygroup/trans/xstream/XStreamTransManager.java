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
package org.tinygroup.trans.xstream;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.xstream.XStreamFactory;
import org.tinygroup.xstream.config.XStreamConfiguration;

import java.io.InputStream;
import java.util.List;

public class XStreamTransManager {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(XStreamTransManager.class);

    public static void initXStream(String filePath) {
        XStream stream = new XStream();
        stream.autodetectAnnotations(true);
        stream.processAnnotations(new Class[]{XStreamConfiguration.class});
        FileObject fileObject = VFS.resolveFile(filePath);
        if (!fileObject.isExist() || fileObject == null) {
            LOGGER.logMessage(LogLevel.ERROR, "文件：{0}不存在", filePath);
            throw new RuntimeException("文件：" + filePath + "不存在");
        }
        InputStream is = fileObject.getInputStream();
        XStreamConfiguration xStreamConfiguration = (XStreamConfiguration) stream
                .fromXML(is);
        try {
            XStreamFactory.parse(xStreamConfiguration);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR,
                    "xstream配置文件初始化失败，文件路径：{0},错误信息：{1}", filePath, e);
            throw new RuntimeException("xstream配置文件:" + filePath + "初始化失败", e);
        } finally {
            VFS.closeInputStream(is);
        }
    }

    public static void initXStream(List<String> filePaths) {
        for (String filePath : filePaths) {
            initXStream(filePath);
        }
    }

}
