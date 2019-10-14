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
package org.tinygroup.templatei18n;

import org.tinygroup.template.TemplateContext;

import java.util.Locale;

/**
 * 从上下文获取Locale
 *
 * @author yancheng11334
 */
public class ContextLocaleI18nVisitor extends AbstractI18nVisitor {

    private String localeName;

    public Locale getLocale(TemplateContext context) {
        return getContextLocale(context);
    }

    public String getLocaleName() {
        return localeName;
    }

    public void setLocaleName(String localeName) {
        this.localeName = localeName;
    }

    protected Locale getContextLocale(TemplateContext context) {
        return (localeName == null || context == null) ? null : (Locale) context.get(localeName);
    }

}
