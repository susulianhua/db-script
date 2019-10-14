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
package org.tinygroup.trans.xstream.base;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.trans.xstream.XStreamTransManager;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XStreamSceneMappingManager {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(XStreamSceneMappingManager.class);
    static Map<String, String> scenePackageName = new HashMap<String, String>();

    public static String getXStreamPackageName(String scene) {
        return scenePackageName.get(scene);
    }

    public static void putSceneName(String scene, String xstreamPackageName) {
        scenePackageName.put(scene, xstreamPackageName);
    }

    public static void removeSceneName(String scene) {
        scenePackageName.remove(scene);
    }

    public static void initXStreamSceneMapping(String filePath) {
        XStream stream = new XStream();
        stream.autodetectAnnotations(true);
        stream.processAnnotations(new Class[]{XStreamSceneMappings.class});
        stream.setClassLoader(XStreamTransManager.class.getClassLoader());
        FileObject fileObject = VFS.resolveFile(filePath);
        if (!fileObject.isExist() || fileObject == null) {
            LOGGER.logMessage(LogLevel.ERROR, "文件：{0}不存在", filePath);
            throw new RuntimeException("文件：" + filePath + "不存在");
        }
        InputStream is = fileObject.getInputStream();
        XStreamSceneMappings mappings = (XStreamSceneMappings) stream
                .fromXML(is);
        VFS.closeInputStream(is);
        List<XStreamSceneMapping> sceneMappings = mappings.getSceneMappings();
        for (XStreamSceneMapping mapping : sceneMappings) {
            putSceneName(mapping.getScene(), mapping.getXstreamPackageName());
        }
    }
}
