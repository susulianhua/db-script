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
package org.tinygroup.templatespringext.springext;

import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.impl.TemplateContextDefault;

import java.util.Map;

/**
 * 模板上下文包装类
 *
 * @author wll
 */
public class TinyWebTemplateContext extends TemplateContextDefault implements
        TemplateContext {

    public TinyWebTemplateContext(Map dataMap) {
        super(dataMap);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) super.get(name);
    }

    @Override
    public boolean exist(String name) {
        return super.exist(name);
    }


}
