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
package org.tinygroup.fulltext.document;

import org.tinygroup.fulltext.FullTextHelper;
import org.tinygroup.fulltext.field.Field;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 抽象文档类
 *
 * @author yancheng11334
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractDocument implements Document {

    protected List<Field> fields = new ArrayList<Field>();

    public Iterator<Field> iterator() {
        return fields.iterator();
    }

    public Field getId() {
        return getField(FullTextHelper.getStoreId());
    }

    public Field getType() {
        return getField(FullTextHelper.getStoreType());
    }

    public Field getTitle() {
        return getField(FullTextHelper.getStoreTitle());
    }

    public Field getAbstract() {
        return getField(FullTextHelper.getStoreAbstract());
    }


    public Field getField(String name) {
        Iterator<Field> it = iterator();
        while (it.hasNext()) {
            Field field = it.next();
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    public List<Field> getFields(String name) {
        List<Field> result = new ArrayList<Field>();
        Iterator<Field> it = iterator();
        while (it.hasNext()) {
            Field field = it.next();
            if (field.getName().equals(name)) {
                result.add(field);
            }
        }
        return result;
    }
}
