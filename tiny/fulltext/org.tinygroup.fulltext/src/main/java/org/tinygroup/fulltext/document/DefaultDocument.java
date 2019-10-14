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

import org.tinygroup.fulltext.field.Field;

import java.util.Iterator;
import java.util.List;

/**
 * 默认的文档对象
 *
 * @author yancheng11334
 */
@SuppressWarnings("rawtypes")
public class DefaultDocument extends AbstractDocument implements Document {

    public void addField(Field field) {
        fields.add(field);
    }

    public void addFields(List<Field> fieldList) {
        fields.addAll(fieldList);
    }

    public void deleteField(String name) {
        Iterator<Field> it = iterator();
        while (it.hasNext()) {
            Field field = it.next();
            if (field.getName().equals(name)) {
                it.remove();
                return;
            }
        }
    }

    public void deleteFields(String name) {
        Iterator<Field> it = iterator();
        while (it.hasNext()) {
            Field field = it.next();
            if (field.getName().equals(name)) {
                it.remove();
            }
        }
    }

}
