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

import org.tinygroup.template.ResourceLoader;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.TemplateException;

/**
 * 抽象模板加载器
 * Created by luoguo on 2014/6/9.
 */
public abstract class AbstractResourceLoader<T> implements ResourceLoader<T> {
    private ClassLoader classLoader;
    private TemplateEngine templateEngine;
    private String templateExtName;
    private String layoutExtName;
    private String macroLibraryExtName;


    public AbstractResourceLoader(String templateExtName, String layoutExtName, String macroLibraryExtName) {
        if (templateExtName != null) {
            this.templateExtName = "." + templateExtName;
        }
        if (layoutExtName != null) {
            this.layoutExtName = "." + layoutExtName;
        }
        if (macroLibraryExtName != null) {
            this.macroLibraryExtName = "." + macroLibraryExtName;
        }
        setClassLoader(getClass().getClassLoader());
    }

    public boolean isTemplate(String path) {
        return templateExtName == null ? false : path.endsWith(templateExtName);
    }

    public boolean isLayout(String path) {
        return layoutExtName == null ? false : path.endsWith(layoutExtName);
    }

    public boolean isMacroLibrary(String path) {
        return macroLibraryExtName == null ? false : path.endsWith(macroLibraryExtName);
    }

    /**
     * 是否引擎加载的资源
     *
     * @param path
     * @return
     */
    public boolean isLoadResource(String path) {
        return isTemplate(path) || isLayout(path) || isMacroLibrary(path);
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String getLayoutPath(String templatePath) {
        if (templateExtName != null && templatePath.endsWith(templateExtName)) {
            return templatePath.substring(0, templatePath.length() - templateExtName.length()) + layoutExtName;
        }
        return null;
    }


    public String getTemplateExtName() {
        return templateExtName;
    }

    public void setTemplateExtName(String templateExtName) {
        this.templateExtName = templateExtName;
    }

    public String getLayoutExtName() {
        return layoutExtName;
    }

    public void setLayoutExtName(String layoutExtName) {
        this.layoutExtName = layoutExtName;
    }

    public String getMacroLibraryExtName() {
        return macroLibraryExtName;
    }

    public void setMacroLibraryExtName(String macroLibraryExtName) {
        this.macroLibraryExtName = macroLibraryExtName;
    }

    public Template getTemplate(String path) throws TemplateException {
        return getTemplateItem(path, templateExtName);
    }

    private Template getTemplateItem(String path, String templateExtName) throws TemplateException {
        if (templateExtName != null && !path.endsWith(templateExtName)) {
            return null;
        }
        Template template = getTemplateEngine().findTemplateCache(path);
        if (template == null || isModified(path)) {
            template = loadTemplateItem(path);
            if (template != null) {
                resetModified(path);
            }
        }
        return template;
    }

    public Template getLayout(String path) throws TemplateException {
        return getTemplateItem(path, layoutExtName);
    }

    public Template getMacroLibrary(String path) throws TemplateException {
        return getTemplateItem(path, macroLibraryExtName);
    }

    protected abstract Template loadTemplateItem(String path) throws TemplateException;

    public ResourceLoader addTemplate(Template template) throws TemplateException {
        return addTemplateItem(template);
    }

    private ResourceLoader addTemplateItem(Template template) throws TemplateException {
        if (!getTemplateEngine().isCheckModified()) {
            getTemplateEngine().addTemplateCache(template.getPath(), template);
        }
        template.setTemplateEngine(templateEngine);
        template.getTemplateContext().setParent(templateEngine.getTemplateContext());
        return this;
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
}
