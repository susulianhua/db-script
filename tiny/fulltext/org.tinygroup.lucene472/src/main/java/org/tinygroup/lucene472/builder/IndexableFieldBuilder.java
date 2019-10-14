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
package org.tinygroup.lucene472.builder;

import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.fulltext.field.StoreField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 构建Lucene的IndexableField对象
 *
 * @author yancheng11334
 */
@SuppressWarnings("rawtypes")
public class IndexableFieldBuilder {

    private static final String HTML_TAG_RULE = "<[/]?[\\s\\S]*?[/]?>";

    public org.apache.lucene.index.IndexableField build(StoreField field, boolean tag) {
        switch (field.getType()) {
            case STRING: {
                return buildStringField(field, tag);
            }
            case INT: {
                return buildIntField(field);
            }
            case LONG: {
                return buildLongField(field);
            }
            case FLOAT: {
                return buildFloatField(field);
            }
            case DOUBLE: {
                return buildDoubleField(field);
            }
            case DATE: {
                return buildDateField(field);
            }
            case BIGDECIMAL: {
                return buildBigDecimalField(field);
            }
            case BINARY: {
                return buildBinaryField(field);
            }
            default: {
                return buildObjectField(field);
            }
        }
    }

    private org.apache.lucene.index.IndexableField buildStringField(
            StoreField field, boolean tag) {
        String v = StringUtil.defaultIfBlank((String) field.getValue(), "");
        v = tag ? v.replaceAll(HTML_TAG_RULE, "") : v;
        if (field.isTokenized()) {
            return new TextField(field.getName(),
                    v, field.isStored() ? Store.YES : Store.NO);
        } else {
            return new StringField(field.getName(),
                    v, field.isStored() ? Store.YES : Store.NO);
        }
    }

    private org.apache.lucene.index.IndexableField buildIntField(
            StoreField field) {
        return new IntField(field.getName(), (Integer) field.getValue(),
                field.isStored() ? Store.YES : Store.NO);
    }

    private org.apache.lucene.index.IndexableField buildLongField(
            StoreField field) {
        return new LongField(field.getName(), (Long) field.getValue(),
                field.isStored() ? Store.YES : Store.NO);
    }

    private org.apache.lucene.index.IndexableField buildFloatField(
            StoreField field) {
        return new FloatField(field.getName(), (Float) field.getValue(),
                field.isStored() ? Store.YES : Store.NO);
    }

    private org.apache.lucene.index.IndexableField buildDoubleField(
            StoreField field) {
        return new DoubleField(field.getName(), (Double) field.getValue(),
                field.isStored() ? Store.YES : Store.NO);
    }

    private org.apache.lucene.index.IndexableField buildDateField(
            StoreField field) {
        Date date = (Date) field.getValue();
        return new StringField(field.getName(), DateTools.dateToString(date,
                DateTools.Resolution.SECOND), field.isStored() ? Store.YES : Store.NO);
    }

    private org.apache.lucene.index.IndexableField buildBigDecimalField(
            StoreField field) {
        BigDecimal bigDecimal = (BigDecimal) field.getValue();
        return new StringField(field.getName(), bigDecimal.toString(),
                field.isStored() ? Store.YES : Store.NO);
    }

    private org.apache.lucene.index.IndexableField buildBinaryField(
            StoreField field) {
        return new org.apache.lucene.document.StoredField(field.getName(),
                (byte[]) field.getValue());
    }

    private org.apache.lucene.index.IndexableField buildObjectField(
            StoreField field) {
        return new org.apache.lucene.document.StoredField(field.getName(),
                field.getValue().toString());
    }
}
