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

import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.impl.TemplateEngineDefault;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by luoguo on 2014/6/9.
 */
public class StringResourceLoader extends AbstractResourceLoader<String> {

    /**
     * 字符串的模板资源
     */
    private Map<String, Template> repositories = new ConcurrentHashMap<String, Template>();

    public StringResourceLoader() {
        super(null, null, null);
    }

    /**
     * 字符串内存型的，不支持根据路径载入
     *
     * @param path
     * @return
     * @throws TemplateException
     */

    protected Template loadTemplateItem(String path) throws TemplateException {
        return null;
    }


    protected Template loadLayout(String path) throws TemplateException {
        return null;
    }

    protected Template loadMacroLibrary(String path) throws TemplateException {
        return null;
    }

    public boolean isModified(String path) {
        return false;
    }

    /**
     * 字符串内存型的，不支持根据路径载入
     *
     * @param path
     * @param encode
     * @return
     */
    public String getResourceContent(String path, String encode) {
        return null;
    }

    /**
     * 根据字符串创建并缓存模板
     *
     * @param stringTemplateMaterial
     * @return
     * @throws TemplateException
     */
    public Template loadTemplate(String stringTemplateMaterial) throws TemplateException {
        if (repositories.containsKey(stringTemplateMaterial)) {
            return repositories.get(stringTemplateMaterial);
        }
        Template template = createTemplate(stringTemplateMaterial);
        if (template != null) {
            repositories.put(stringTemplateMaterial, template);
        }
        return template;
    }

    /**
     * 根据字符串创建模板(不缓存)
     */
    public Template createTemplate(String stringTemplateMaterial) throws TemplateException {
        try {
            String rPath = getRandomPath();
            Template template = TemplateLoadUtil.loadComponent((TemplateEngineDefault) getTemplateEngine(), rPath, rPath, System.currentTimeMillis(), stringTemplateMaterial);
            template.setTemplateEngine(getTemplateEngine());
            return template;
        } catch (Exception e) {
            throw new TemplateException(e);
        }
        //这里没有调用putTemplate是避免内存泄露
    }

    public Template createLayout(String templateMaterial) throws TemplateException {
        throw new TemplateException("Not supported.");
    }

    public Template createMacroLibrary(String templateMaterial) throws TemplateException {
        throw new TemplateException("Not supported.");
    }

    private String getRandomPath() {
        return "C" + System.nanoTime();
    }

    public void resetModified(String path) {

    }


}
