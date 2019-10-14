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
package org.tinygroup.lucene472.manage;

import org.tinygroup.fulltext.exception.FullTextException;
import org.tinygroup.lucene472.LuceneDynamicManager;
import org.tinygroup.lucene472.config.LuceneConfig;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.TemplateRender;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.impl.TemplateRenderDefault;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 支持动态配置的lucene配置管理器
 *
 * @author yancheng11334
 */
public class LuceneDynamicManagerImpl implements LuceneDynamicManager {

    private Map<String, LuceneConfig> configMaps = new LinkedHashMap<String, LuceneConfig>();
    private TemplateRender templateRender = null;

    public LuceneDynamicManagerImpl() {
        TemplateEngine engine = new TemplateEngineDefault();
        templateRender = new TemplateRenderDefault();
        templateRender.setTemplateEngine(engine);
    }

    public void addFullTextConfig(LuceneConfig config) {
        if (config != null) {
            configMaps.put(config.getId(), config);
        }
    }

    public void removeFullTextConfig(LuceneConfig config) {
        if (config != null) {
            configMaps.remove(config.getId());
        }
    }

    private LuceneConfig getLuceneConfig(String configId) {
        if (configId != null) {
            return configMaps.get(configId);
        }

        Iterator<String> it = configMaps.keySet().iterator();
        if (it.hasNext()) {
            String key = it.next();
            return configMaps.get(key);
        }
        return null;
    }

    public LuceneConfig getFullTextConfig(String userId, String configId) {
        LuceneConfig config = getLuceneConfig(configId);
        if (config == null) {
            throw new FullTextException(String.format("获取动态配置失败:根据[%s]没有找到对应的动态Lucene配置", configId));
        }
        //原始config不能修改
        LuceneConfig newConfig = (LuceneConfig) config.clone();

        TemplateContext context = new TemplateContextDefault();
        context.put("userId", userId);
        //目前只需要渲染路径属性
        try {
            String s = templateRender.renderTemplateContent(newConfig.getDirectory(), context);
            newConfig.setDirectory(s);
        } catch (TemplateException e) {
            throw new FullTextException("获取动态配置失败:模板渲染发生异常", e);
        }
        return newConfig;
    }

    public LuceneConfig getFullTextConfig(String userId) {
        return getFullTextConfig(userId, null);
    }


}
