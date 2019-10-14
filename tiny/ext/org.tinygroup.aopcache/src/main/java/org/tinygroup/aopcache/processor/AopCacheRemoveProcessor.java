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
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.template.TemplateContext;

/**
 * aop缓存删除操作
 *
 * @author renhui
 */
public class AopCacheRemoveProcessor extends AbstractAopCacheProcessor {

    @Override
    public void postProcess(CacheMetadata metadata,
                            MethodInvocation invocation, Object result) {
        TemplateRender templateRender = TemplateUtil.getTemplateRender();
        try {
            TemplateContext templateContext = templateRender.assemblyContext(invocation);
            String group = templateRender.renderTemplate(templateContext, metadata.getGroup());
            String removeKeys = templateRender.renderTemplate(templateContext, metadata.getRemoveKeys());
            String removeGroups = templateRender.renderTemplate(templateContext, metadata.getRemoveGroups());
            if (!StringUtil.isBlank(removeKeys)) {
                String[] removeArray = removeKeys.split(SPLIT_KEY);
                for (String removeStr : removeArray) {
                    getAopCache().remove(group, removeStr);
                }
            }
            if (!StringUtil.isBlank(removeGroups)) {
                String[] removeGroupArray = removeGroups.split(SPLIT_KEY);
                for (String removeGroupStr : removeGroupArray) {
                    getAopCache().cleanGroup(removeGroupStr);
                }
            }

        } catch (Throwable e) {
            throw new AopCacheException(e);
        }
    }
}
