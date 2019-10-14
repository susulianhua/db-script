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
package org.tinygroup.fulltext.field;

import java.util.Date;

public class DateField extends AbstractField<Date> implements StoreField<Date> {

    public DateField(String name, Date value) {
        this(name, value, true, true, false);
    }

    public DateField(String name, Date value, boolean indexed, boolean stored, boolean tokenized) {
        super(name, value);
        this.setIndexed(indexed);
        this.setStored(stored);
        this.setTokenized(tokenized);
    }

    public FieldType getType() {
        return FieldType.DATE;
    }


    public String toString() {
        return "DateField [name=" + getName() + ", value="
                + getValue() + "]";
    }
}
