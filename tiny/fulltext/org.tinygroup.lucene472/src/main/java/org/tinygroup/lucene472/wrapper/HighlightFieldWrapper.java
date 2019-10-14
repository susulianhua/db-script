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
package org.tinygroup.lucene472.wrapper;

import org.apache.lucene.index.IndexableField;
import org.tinygroup.fulltext.exception.FullTextException;
import org.tinygroup.fulltext.field.Field;
import org.tinygroup.fulltext.field.FieldType;
import org.tinygroup.fulltext.field.HighlightField;
import org.tinygroup.fulltext.field.StringField;

@SuppressWarnings("rawtypes")
public class HighlightFieldWrapper implements HighlightField {

    private Field field;
    private String perfix;
    private String suffix;
    private String template;

    public HighlightFieldWrapper(IndexableField indexableField, String perfix, String suffix, String template) {
        field = new StringField(indexableField.name(), indexableField.stringValue());
        this.perfix = perfix;
        this.suffix = suffix;
        this.template = template;
    }

    public String getName() {
        return field.getName();
    }

    public FieldType getType() {
        return field.getType();
    }

    public Object getValue() {
        return field.getValue();
    }

    public Object getSourceValue() {
        return getRenderValue("", "");
    }

    public Object getRenderValue(Object... arguments) {
        try {
            String pword = (String) arguments[0];
            String sword = (String) arguments[1];

            String text = template.toString();
            return text.replaceAll(perfix, pword).replaceAll(suffix, sword);
        } catch (Exception e) {
            throw new FullTextException("高亮字段渲染失败:参数格式不正确", e);
        }
    }

    public String toString() {
        return "HighlightFieldWrapper [perfix=" + perfix + ", suffix=" + suffix
                + ", name=" + field.getName() + ", template=" + template + "]";
    }

}
