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
package org.tinygroup.chinese.applicationprocessor;

import org.tinygroup.application.AbstractApplicationProcessor;
import org.tinygroup.chinese.fileProcessor.ChineseContainer;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.List;

public class ChineseWordApplicationProcessor extends AbstractApplicationProcessor {
    private static final String FILE_CHINESE_NODE_PATH = "/application/chinese";
    private static final String SCENE_TAG = "scene";
    private XmlNode appConfig;

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        appConfig = applicationConfig;
    }

    public XmlNode getApplicationConfig() {
        return appConfig;
    }

    public String getApplicationNodePath() {
        return FILE_CHINESE_NODE_PATH;
    }

    public XmlNode getComponentConfig() {
        return null;
    }

    public String getComponentConfigPath() {
        return null;
    }

    public int getOrder() {
        return 0;
    }

    public void init() {
        if (appConfig == null) {
            return;
        }
        List<XmlNode> scenes = appConfig.getSubNodesRecursively(SCENE_TAG);
        for (XmlNode scene : scenes) {
            String sceneName = scene.getAttribute("name");
            String type = scene.getAttribute("type");
            String wordNames = scene.getAttribute("words");
            ChineseContainer.RegScene(sceneName, wordNames, type);
        }
    }


    public void start() {


    }

    public void stop() {


    }

}
