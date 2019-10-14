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
package org.tinygroup.fileresolver.impl;

import org.tinygroup.config.Configuration;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全路径资源提供器实现类
 *
 * @author yanwj06282
 */
public class ExcludeContextFileFinder implements
        Configuration {
    private static final String FILE = "file";
    private static final String CONTENT_TYPE = "content-type";
    private static final String EXT_NAME = "ext-name";
    private static final String EXCLUDE_FULL_CONTEXT_FILE_FINDER_PATH = "/application/file-resolver-configuration/exclude-full-context-file-finder";
    protected XmlNode applicationConfig;
    protected XmlNode componentConfig;
    // 保存文件类型列表
    private Map<String, String> excludeFileExtensionMap = new HashMap<String, String>();

    public boolean checkMatch(FileObject fileObject) {
        return !(fileObject != null && excludeFileExtensionMap.containsKey(fileObject.getExtName()));
    }


    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        this.applicationConfig = applicationConfig;
        this.componentConfig = componentConfig;
        List<XmlNode> fileNodes = ConfigurationUtil.combineSubList(FILE,
                applicationConfig, componentConfig);
        for (XmlNode fileNode : fileNodes) {
            String extName = fileNode.getAttribute(EXT_NAME);
            String contentType = fileNode.getAttribute(CONTENT_TYPE);
            excludeFileExtensionMap.put(extName, contentType);
        }
    }

    public String getApplicationNodePath() {
        return EXCLUDE_FULL_CONTEXT_FILE_FINDER_PATH;
    }

    public Map<String, String> getExcludeFileExtensionMap() {
        return excludeFileExtensionMap;
    }


    public String getComponentConfigPath() {
        return null;
    }

    public XmlNode getComponentConfig() {
        return componentConfig;
    }

    public XmlNode getApplicationConfig() {
        return applicationConfig;
    }


}
