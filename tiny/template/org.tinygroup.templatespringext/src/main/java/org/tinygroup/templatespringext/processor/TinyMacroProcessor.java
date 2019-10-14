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
 * Created by wangll13383 on 2015/9/9.
 * macro文件处理器
 */
public class TinyMacroProcessor extends AbstractFileProcessor {

    private final static String MACRO_FILE_EXT = ".component";

    private TemplateEngine engine;

    public TemplateEngine getEngine() {
        return engine;
    }

    public void setEngine(TemplateEngine engine) {
        this.engine = engine;
    }

    public void process() {
        if (fileList.size() > 0) {
            LOGGER.logMessage(LogLevel.INFO, "开始加载Macro文件...");
            for (FileObject file : fileList) {
                getFileResourceManager().addResources(engine, file, ".page", ".layout", ".component");
            }
            LOGGER.logMessage(LogLevel.INFO, "加载Macro文件结束...");
        }
    }

    public boolean isMatch(String fileName) {
        return fileName.endsWith(MACRO_FILE_EXT);
    }

    public String getPath(FileObject fileObject) {
        String absolutePath = fileObject.getAbsolutePath();
        if (absolutePath.contains("resources")) {
            return modifyPath(fileObject, "resources").substring(1);
        }
        if (absolutePath.contains("classes")) {
            return modifyPath(fileObject, "classes").substring(1);
        }
        return fileObject.getPath();
    }

    public String modifyPath(FileObject file, String parent) {
        if (!parent.equals(file.getFileName())) {
            return modifyPath(file.getParent(), parent) + "/" + file.getFileName();
        }
        return "";
    }
}
