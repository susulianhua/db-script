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

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.fulltext.FullTextHelper;
import org.tinygroup.fulltext.document.DefaultDocument;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.fulltext.exception.FullTextException;
import org.tinygroup.fulltext.field.Field;
import org.tinygroup.fulltext.field.StringField;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.templateindex.config.IndexFieldConfig;

import java.util.List;

/**
 * 动态字段文档创建
 *
 * @author yancheng11334
 */
public class DynamicDocumentCreator extends AbstractTemplateIndexRender {

    private List<IndexFieldConfig> fieldConfigs;

    public DynamicDocumentCreator() {
        super();
    }

    public DynamicDocumentCreator(List<IndexFieldConfig> indexFieldConfigs) {
        super();
        this.fieldConfigs = indexFieldConfigs;
    }

    public Document execute(Context context) {
        TemplateContext templateContext = new TemplateContextDefault();
        templateContext.setParent(context);
        DefaultDocument document = new DefaultDocument();
        try {
            if (fieldConfigs != null) {
                for (IndexFieldConfig fieldConfig : fieldConfigs) {
                    String value = getTemplateRender().renderTemplateContent(
                            fieldConfig.getTemplateRule(), templateContext);
                    document.addField(createField(fieldConfig, value));
                }
            }
        } catch (Exception e) {
            throw new FullTextException(e);
        } finally {
            templateContext.setParent(null);
        }
        return document;
    }

    @SuppressWarnings("rawtypes")
    private Field createField(IndexFieldConfig fieldConfig, String value) {
        boolean indexed = getSafeBoolean(fieldConfig.getIndexed(), true);
        boolean stored = getSafeBoolean(fieldConfig.getStored(), true);

        // 是否分词比较复杂，默认id和type是不分词；title、abstract和用户扩展字段是要分词的
        boolean tokenized;
        String name = fieldConfig.getIndexName();
        if (FullTextHelper.getStoreId().equals(name)
                || FullTextHelper.getStoreType().equals(name)) {
            tokenized = getSafeBoolean(fieldConfig.getTokenized(), false);
        } else {
            tokenized = getSafeBoolean(fieldConfig.getTokenized(), true);
        }
        return new StringField(name, value, indexed, stored, tokenized);
    }

    private boolean getSafeBoolean(String str, boolean tag) {
        if (StringUtil.isEmpty(str)) {
            return tag;
        } else {
            return Boolean.parseBoolean(str);
        }
    }

}
