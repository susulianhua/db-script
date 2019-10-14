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
package org.tinygroup.trans.template.manager;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.template.TemplateRender;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.impl.TemplateRenderDefault;
import org.tinygroup.trans.template.ClassScriptMapping;
import org.tinygroup.trans.template.SceneMapping;
import org.tinygroup.trans.template.TemplateConverts;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateConvertManager {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(TemplateConvertManager.class);
    private static Map<String, Map<String, String>> mappingMap = new HashMap<String, Map<String, String>>();
    private static TemplateRender render;

    static {
        render = new TemplateRenderDefault();
        render.setTemplateEngine(new TemplateEngineDefault());
    }

    public static TemplateRender getRender() {
        return render;
    }

    public static String getScript(String classpath, String scene) {
        if (mappingMap.containsKey(scene)) {
            return mappingMap.get(scene).get(classpath);
        }
        return null;
    }

    public static void put(List<String> filePaths) {
        for (String filePath : filePaths) {
            put(filePath);
        }
    }

    public static void put(String filePath) {
        XStream stream = new XStream();
        stream.autodetectAnnotations(true);
        stream.processAnnotations(new Class[]{TemplateConverts.class});
        stream.setClassLoader(TemplateConvertManager.class.getClassLoader());
        FileObject fileObject = VFS.resolveFile(filePath);
        if (!fileObject.isExist() || fileObject == null) {
            LOGGER.logMessage(LogLevel.ERROR, "文件：{0}不存在", filePath);
            throw new RuntimeException("文件：" + filePath + "不存在");
        }
        InputStream is = fileObject.getInputStream();
        TemplateConverts templateConverts = (TemplateConverts) stream
                .fromXML(is);
        put(templateConverts);
        VFS.closeInputStream(is);
    }

    public static void put(TemplateConverts converts) {
        List<SceneMapping> sceneMappings = converts.getSceneMappings();
        for (SceneMapping sceneMapping : sceneMappings) {
            LOGGER.logMessage(LogLevel.INFO, "开始加载场景：{0}",
                    sceneMapping.getScene());
            Map<String, String> map;
            if (mappingMap.containsKey(sceneMapping.getScene())) {
                map = mappingMap.get(sceneMapping.getScene());
                initClassScriptMapping(map,
                        sceneMapping.getClassScriptMappings());
            } else {
                map = new HashMap<String, String>();
                initClassScriptMapping(map,
                        sceneMapping.getClassScriptMappings());
                mappingMap.put(sceneMapping.getScene(), map);
            }
            LOGGER.logMessage(LogLevel.INFO, "场景：{0}加载完毕",
                    sceneMapping.getScene());
        }
    }

    private static void initClassScriptMapping(Map<String, String> map,
                                               List<ClassScriptMapping> classScriptMappings) {
        for (ClassScriptMapping classScriptMapping : classScriptMappings) {
            LOGGER.logMessage(LogLevel.INFO, "开始加载对象：{0}，脚本路径：{1}",
                    classScriptMapping.getClassPath(),
                    classScriptMapping.getScriptPath());
            // FileObject fileObject = VFS.resolveFile(classScriptMapping
            // .getScriptPath());
            String script;
            // InputStream is = fileObject.getInputStream();
            InputStream is = TemplateConvertManager.class
                    .getResourceAsStream(classScriptMapping.getScriptPath());
            try {
                script = StreamUtil.readText(is, "utf-8", true);
                map.put(classScriptMapping.getClassPath(), script);
                LOGGER.logMessage(LogLevel.INFO, "加载对象：{0}，脚本路径：{1}完毕",
                        classScriptMapping.getClassPath(),
                        classScriptMapping.getScriptPath());
            } catch (Exception e) {
                LOGGER.logMessage(LogLevel.ERROR,
                        "加载对象：{0}，脚本路径：{1}失败。错误信息如下：{2}",
                        classScriptMapping.getClassPath(),
                        classScriptMapping.getScriptPath(), e);
                throw new RuntimeException("加载对象："
                        + classScriptMapping.getClassPath() + "，脚本路径："
                        + classScriptMapping.getScriptPath() + "失败", e);
            } finally {
                VFS.closeInputStream(is);
            }
        }
    }

    public static void remove(TemplateConverts converts) {
        List<SceneMapping> sceneMappings = converts.getSceneMappings();
        for (SceneMapping mapping : sceneMappings) {
            mappingMap.remove(mapping.getScene());
        }
    }

}
