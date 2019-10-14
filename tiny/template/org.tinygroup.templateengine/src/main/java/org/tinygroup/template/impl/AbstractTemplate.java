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
package org.tinygroup.template.impl;

import org.tinygroup.template.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luoguo on 2014/6/6.
 */
public abstract class AbstractTemplate implements Template {
    protected TemplateEngine templateEngine;
    private Map<String, Macro> macroMap = new HashMap<String, Macro>();
    private TemplateContext templateContext = new TemplateContextDefault();
    private List<String> importPathList = new ArrayList<String>();
    private long lastModifiedTime;
    private String absolutePath;

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        for (Macro macro : macroMap.values()) {
            macro.setTemplateEngine(templateEngine);
        }
    }

    public void addImport(Object importPath) {
        importPathList.add(importPath.toString());
    }

    public List<String> getImportPathList() {
        return importPathList;
    }

    public void addMacro(Macro macro) {
        macroMap.put(macro.getName(), macro);
        macro.setTemplateEngine(getTemplateEngine());
    }

    public Map<String, Macro> getMacroMap() {
        return macroMap;
    }

    public void render(TemplateContext context, OutputStream outputStream) throws TemplateException {
        try {
            renderContent(context, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new TemplateException(e);
        } finally {
            context.removeSubContext("$currentTemplateContext");
        }
    }

    public void render(TemplateContext context) throws TemplateException {
        render(context, System.out);
    }

    public void render() throws TemplateException {
        render(new TemplateContextDefault(), System.out);
    }

    protected abstract void renderContent(TemplateContext context, OutputStream outputStream) throws IOException, TemplateException;

    public TemplateContext getTemplateContext() {
        return templateContext;
    }


    public Object put(String key, Object value) {
        return templateContext.put(key, value);
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long time) {
        this.lastModifiedTime = time;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String path) {
        this.absolutePath = path;
    }

}
