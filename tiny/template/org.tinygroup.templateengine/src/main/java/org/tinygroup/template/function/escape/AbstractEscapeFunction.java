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
package org.tinygroup.template.function.escape;

import org.apache.commons.lang.StringUtils;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.function.AbstractTemplateFunction;

/**
 * 抽象的转义/反转义基类
 * @author yancheng11334
 *
 */
public abstract class AbstractEscapeFunction extends AbstractTemplateFunction {

    protected String[] searchWords;
    protected String[] replaceWords;

    public AbstractEscapeFunction(String names) {
        super(names);
    }

    public String[] getSearchWords() {
        return searchWords;
    }

    public void setSearchWords(String[] searchWords) {
        this.searchWords = searchWords;
    }

    public String[] getReplaceWords() {
        return replaceWords;
    }

    public void setReplaceWords(String[] replaceWords) {
        this.replaceWords = replaceWords;
    }

    public Object execute(Template template, TemplateContext context, Object... parameters) throws TemplateException {
        if (parameters == null || parameters.length < 1) {
            throw new TemplateException(String.format("函数%s参数格式不正确.", getNames()));
        }
        if (parameters[0] == null) {
            return null; //直接返回
        }
        String content = parameters[0].toString();
        if (StringUtil.isEmpty(content)) {
            return content;
        }
        return replace(content);
    }


    /**
     * 动态替换
     * @param content
     * @return
     */
    protected String replace(String content) throws TemplateException {
        try {
            return StringUtils.replaceEach(content, searchWords, replaceWords);
        } catch (Exception e) {
            throw new TemplateException(e);
        }

    }

}
