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

import org.tinygroup.context.Context;
import org.tinygroup.fulltext.FullTextHelper;
import org.tinygroup.fulltext.document.DefaultDocument;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.fulltext.exception.FullTextException;
import org.tinygroup.fulltext.field.Field;
import org.tinygroup.fulltext.field.StringField;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.impl.TemplateContextDefault;

import java.util.Map.Entry;

/**
 * 基于模板引擎的文档创建，简化操作规则
 *
 * @author yancheng11334
 */
@SuppressWarnings("rawtypes")
public class TemplateDocumentCreator extends AbstractTemplateIndexRender {


    private String idRule;
    private String typeRule;
    private String titleRule;
    private String abstractRule;

    private boolean initTag = false;

    public TemplateDocumentCreator() {
        super();
    }

    public TemplateDocumentCreator(String idRule, String typeRule,
                                   String titleRule, String abstractRule) {
        super();
        this.idRule = idRule;
        this.typeRule = typeRule;
        this.titleRule = titleRule;
        this.abstractRule = abstractRule;
    }

    public String getIdRule() {
        return idRule;
    }

    public void setIdRule(String idRule) {
        this.idRule = idRule;
    }

    public String getTypeRule() {
        return typeRule;
    }

    public void setTypeRule(String typeRule) {
        this.typeRule = typeRule;
    }

    public String getTitleRule() {
        return titleRule;
    }

    public void setTitleRule(String titleRule) {
        this.titleRule = titleRule;
    }

    public String getAbstractRule() {
        return abstractRule;
    }

    public void setAbstractRule(String abstractRule) {
        this.abstractRule = abstractRule;
    }

    /**
     * 执行渲染逻辑
     *
     * @param context
     * @return
     */
    public Document execute(Context context) {
        if (!initTag) {
            addTemplate(idRule);
            addTemplate(typeRule);
            addTemplate(titleRule);
            addTemplate(abstractRule);
            initTag = true;
        }
        TemplateContext templateContext = new TemplateContextDefault();
        templateContext.setParent(context);
        DefaultDocument document = new DefaultDocument();
        try {
            document.addField(renderId(templateContext));
            document.addField(renderType(templateContext));
            document.addField(renderTitle(templateContext));
            document.addField(renderAbstarctRule(templateContext));
            //执行其他字段
            for (Entry<String, Object> entry : context.getItemMap().entrySet()) {
                String s = entry.getKey();
                if (s.equals(FullTextHelper.getStoreId()) || s.equals(FullTextHelper.getStoreType()) || s.equals(FullTextHelper.getStoreTitle()) || s.equals(FullTextHelper.getStoreAbstract())) {
                    continue;
                } else {
                    document.addField(new StringField(entry.getKey(), entry.getValue().toString(), true, true, true));
                }
            }
        } catch (Exception e) {
            throw new FullTextException(e);
        } finally {
            templateContext.setParent(null);
        }
        return document;
    }

    protected Field renderId(TemplateContext templateContext) throws TemplateException {
        String value = getTemplateRender().renderTemplateWithOutLayout(idRule, templateContext);
        return new StringField(FullTextHelper.getStoreId(), value);
    }

    protected Field renderType(TemplateContext templateContext) throws TemplateException {
        String value = getTemplateRender().renderTemplateWithOutLayout(typeRule, templateContext);
        return new StringField(FullTextHelper.getStoreType(), value);
    }

    protected Field renderTitle(TemplateContext templateContext) throws TemplateException {
        String value = getTemplateRender().renderTemplateWithOutLayout(titleRule, templateContext);
        return new StringField(FullTextHelper.getStoreTitle(), value, true, true, true);
    }

    protected Field renderAbstarctRule(TemplateContext templateContext) throws TemplateException {
        String value = getTemplateRender().renderTemplateWithOutLayout(abstractRule, templateContext);
        return new StringField(FullTextHelper.getStoreAbstract(), value, true, true, true);
    }
}
