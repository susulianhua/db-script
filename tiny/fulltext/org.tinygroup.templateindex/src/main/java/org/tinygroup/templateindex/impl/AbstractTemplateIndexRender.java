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
package org.tinygroup.templateindex.impl;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateRender;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.loader.FileResourceManager;
import org.tinygroup.template.loader.TemplateLoadUtil;
import org.tinygroup.templateindex.TemplateIndexRender;
import org.tinygroup.vfs.FileObject;

/**
 * 抽象模板索引渲染器
 *
 * @author yancheng11334
 */
public abstract class AbstractTemplateIndexRender implements
        TemplateIndexRender {

    private static final String DEFALUT_TEMPLATE_RENDER = "fullTextTemplateRender";
    private static final String DEFALUT_TEMPLATE_FILEMANAGER = "fulltextFileResourceManager";
    private TemplateRender templateRender;

    public FileResourceManager getFileResourceManager() {
        return BeanContainerFactory.getBeanContainer(
                getClass().getClassLoader()).getBean(
                DEFALUT_TEMPLATE_FILEMANAGER);
    }

    public TemplateRender getTemplateRender() {
        if (templateRender == null) {
            return BeanContainerFactory.getBeanContainer(
                    getClass().getClassLoader()).getBean(
                    DEFALUT_TEMPLATE_RENDER);
        }
        return templateRender;
    }

    public void setTemplateRender(TemplateRender templateRender) {
        this.templateRender = templateRender;
    }

    protected void addTemplate(String path) {
        try {
            FileResourceManager manager = getFileResourceManager();
            FileObject fileObject = manager.getOtherFileObject(path);
            manager.addResource(fileObject.getPath(), fileObject);
            Template template = TemplateLoadUtil.loadComponent((TemplateEngineDefault) templateRender.getTemplateEngine(), fileObject);
            templateRender.getTemplateEngine().addTemplateCache(path, template);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
