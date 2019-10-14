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
package org.tinygroup.template.loader;

import org.tinygroup.commons.file.IOUtils;
import org.tinygroup.template.Template;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.interpret.TemplateFromContext;
import org.tinygroup.template.interpret.TemplateInterpreter;
import org.tinygroup.template.interpret.context.ImportProcessor;
import org.tinygroup.template.interpret.context.MacroDefineProcessor;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;
import org.tinygroup.vfs.FileObject;

import java.io.InputStream;

/**
 * 载入组件文件,组件文件需要识别宏与import指令
 * Created by luog on 15/7/20.
 */
public final class TemplateLoadUtil {
    static TemplateInterpreter interpreter = new TemplateInterpreter();

    static {
        interpreter.addContextProcessor(new MacroDefineProcessor());
        interpreter.addContextProcessor(new ImportProcessor());
    }

    public static Template loadComponent(TemplateEngineDefault engine, String path, String absolutePath, long lastModifiedTime, String content) throws Exception {
        TinyTemplateParser.TemplateContext tree = interpreter.parserTemplateTree(path, content, engine.isThrowLexerError());
        TemplateFromContext template = new TemplateFromContext(path, tree);
        template.setAbsolutePath(absolutePath);
        template.setLastModifiedTime(lastModifiedTime);
        interpreter.interpretTree(engine, template, tree, null, null, null, path);
        return template;
    }

    public static Template loadComponent(TemplateEngineDefault engine, FileObject fileObject) throws Exception {
        InputStream inputStream = fileObject.getInputStream();
        try {
            String content = IOUtils.readFromInputStream(inputStream, engine.getEncode());
            return loadComponent(engine, fileObject.getPath(), fileObject.getAbsolutePath(), fileObject.getLastModifiedTime(), content);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

    }
}
