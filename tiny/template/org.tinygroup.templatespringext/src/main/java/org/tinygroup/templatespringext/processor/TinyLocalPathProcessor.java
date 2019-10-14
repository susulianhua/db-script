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
package org.tinygroup.templatespringext.processor;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.templatespringext.AbstractFileProcessor;
import org.tinygroup.vfs.FileObject;

/**
 * Created by wangll13383 on 2015/9/12.
 */
public class TinyLocalPathProcessor extends AbstractFileProcessor {
    private final static String CLASSES_FILE_EXT = "classes";

    private final static String RESOURCE_FILE_EXT = "resources";

    private final static String WEB_INF_FILE_EXT = "WEB-INF";

    private TemplateEngine engine;

    public TemplateEngine getEngine() {
        return engine;
    }

    public void setEngine(TemplateEngine engine) {
        this.engine = engine;
    }

    public boolean isMatch(String fileName) {
        return fileName.equals(CLASSES_FILE_EXT) || fileName.equals(RESOURCE_FILE_EXT) || fileName.equals(WEB_INF_FILE_EXT);
    }

    public void process() {
        if (fileList.size() > 0) {
            LOGGER.logMessage(LogLevel.INFO, "开始加载Path路径...");
            for (FileObject file : fileList) {
                getFileResourceManager().addResources(engine, file, ".page", ".layout", ".component");
            }
            LOGGER.logMessage(LogLevel.INFO, "加载Path路径结束...");
        }
    }
}
