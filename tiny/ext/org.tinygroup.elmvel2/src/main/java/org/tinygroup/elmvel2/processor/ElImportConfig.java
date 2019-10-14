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
package org.tinygroup.elmvel2.processor;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.el.ElImportContainer;
import org.tinygroup.xmlparser.node.XmlNode;

import java.lang.reflect.Method;
import java.util.List;

public class ElImportConfig extends AbstractConfiguration {
    private static final String EL_IMPORT_CONFIG_PATH = "/application/el-import-config";

    public String getApplicationNodePath() {
        return EL_IMPORT_CONFIG_PATH;
    }

    public String getComponentConfigPath() {
        // TODO Auto-generated method stub
        return null;
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        super.config(applicationConfig, componentConfig);
        XmlNode appConfig = getApplicationConfig();
        List<XmlNode> configs = appConfig.getSubNodes("import");
        for (XmlNode config : configs) {
            String key = config.getAttribute("key");
            String classPath = config.getAttribute("class");
            String method = config.getAttribute("method");
            try {
                config(key, classPath, method);
            } catch (Exception e) {
                throw new RuntimeException("初始化EL Import配置出错", e);
            }

        }
    }

    private void config(String key, String classPath, String methodName)
            throws ClassNotFoundException {
        if (StringUtil.isBlank(classPath) || StringUtil.isBlank(key)) {
            return;
        }
        Class<?> clazz = Class.forName(classPath);
        if (StringUtil.isBlank(methodName)) {
            ElImportContainer.addImport(key, clazz);
            return;
        }
        for (Method method : clazz.getMethods()) {
            if (methodName.equals(method.getName())) {
                ElImportContainer.addImport(key, method);
                return;
            }
        }

    }

}
