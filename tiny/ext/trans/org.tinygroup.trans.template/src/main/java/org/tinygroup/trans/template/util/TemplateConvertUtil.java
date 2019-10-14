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
package org.tinygroup.trans.template.util;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.trans.template.manager.TemplateConvertManager;

public class TemplateConvertUtil {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(TemplateConvertUtil.class);
    private static String TEMPLATE_VAR_NAME = "var";

    public static void setVarName(String varName) {
        TEMPLATE_VAR_NAME = varName;
    }

    public static String convert(Object param, String scene) {
        String classpath = param.getClass().getName();
        String scrpit = TemplateConvertManager.getScript(classpath, scene);
        if (StringUtil.isBlank(scrpit)) {
            LOGGER.logMessage(LogLevel.ERROR, "类型：{0}在场景：{1}中转换失败。未找到对应的转换脚本",
                    classpath, scene);
            throw new RuntimeException("类型：" + classpath + "在场景：" + scene
                    + "中转换失败。未找到对应的转换脚本");
        }
        try {
            TemplateContext t = new TemplateContextDefault();
            t.put(String.valueOf(TEMPLATE_VAR_NAME), param);
            return TemplateConvertManager.getRender().renderTemplateContent(
                    scrpit, t);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "类型：{0}在场景：{1}中转换失败，错误信息如下：{2}",
                    classpath, scene, e);
            throw new RuntimeException("类型：" + classpath + "在场景：" + scene
                    + "中转换失败", e);
        }
    }

}
