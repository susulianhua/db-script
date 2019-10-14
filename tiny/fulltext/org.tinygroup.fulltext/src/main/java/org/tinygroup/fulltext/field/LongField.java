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

public class LongField extends AbstractField<Long> implements StoreField<Long> {

    public LongField(String name, Long value) {
        this(name, value, true, true, false);
    }

    public LongField(String name, String value) {
        this(name, value, true, true, false);
    }

    public LongField(String name, Long value, boolean indexed, boolean stored, boolean tokenized) {
        super(name, value);
        this.setIndexed(indexed);
        this.setStored(stored);
        this.setTokenized(tokenized);
    }

    public LongField(String name, String value, boolean indexed, boolean stored, boolean tokenized) {
        this(name, Long.parseLong(value), indexed, stored, tokenized);
    }

    public FieldType getType() {
        return FieldType.LONG;
    }


    public String toString() {
        return "LongField [name=" + getName() + ", value="
                + getValue() + "]";
    }


}
