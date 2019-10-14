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
package org.tinygroup.aopcache.processor;

import org.aopalliance.intercept.MethodInvocation;
import org.tinygroup.aopcache.base.CacheMetadata;
import org.tinygroup.aopcache.base.TemplateRender;
import org.tinygroup.aopcache.exception.AopCacheException;
import org.tinygroup.aopcache.util.TemplateUtil;
import org.tinygroup.template.TemplateContext;


/**
 * 缓存获取操作
 *
 * @author renhui
 */
public class AopCacheGetProcessor extends AbstractAopCacheProcessor {

    @Override
    public boolean doPreProcess(CacheMetadata metadata, MethodInvocation invocation) {
        return false;
    }

    @Override
    public Object endProcessor(CacheMetadata metadata, MethodInvocation invocation) {
        TemplateRender templateRender = TemplateUtil.getTemplateRender();
        try {
            TemplateContext templateContext = templateRender.assemblyContext(invocation);
            String group = templateRender.renderTemplate(templateContext, metadata.getGroup());
            String keys = templateRender.renderTemplate(templateContext, metadata.getKeys());
            String[] keyArray = keys.split(SPLIT_KEY);
            Object result = null;
            for (String key : keyArray) {
                result = getAopCache().get(group, key);
                if (result != null) {
                    return result;
                }
            }
            if (result == null) {
                result = invocation.proceed();
                if (result != null) {
                    for (String key : keyArray) {
                        getAopCache().put(group, key, result);
                    }
                }
            }
            return result;
        } catch (Throwable e) {
            throw new AopCacheException(e);
        }
    }


}
