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

import org.tinygroup.i18n.I18nMessage;
import org.tinygroup.i18n.I18nMessageFactory;
import org.tinygroup.template.I18nVisitor;
import org.tinygroup.template.TemplateContext;

/**
 * 通过I18nMessage实现模板引擎的国际化(第一种方案)
 *
 * @author yancheng11334
 */
public abstract class AbstractI18nVisitor implements I18nVisitor {

    private I18nMessage i18nMessage;

    public I18nMessage getI18nMessage() {
        return i18nMessage == null ? I18nMessageFactory.getI18nMessages() : i18nMessage;
    }

    public void setI18nMessage(I18nMessage i18nMessage) {
        this.i18nMessage = i18nMessage;
    }

    public String getI18nMessage(TemplateContext context, String key) {
        return getI18nMessage().getMessage(key, context, getLocale(context));
    }

}
