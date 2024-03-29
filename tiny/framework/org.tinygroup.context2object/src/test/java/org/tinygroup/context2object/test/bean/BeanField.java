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
package org.tinygroup.context2object.test.bean;

public class BeanField {
    String name;
    BeanNoField field;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameNoExist() {
        return name;
    }

    public void setNameNoExist(String name) {
        this.name = name;
    }

    public BeanNoField getField() {
        return field;
    }

    public void setField(BeanNoField field) {
        this.field = field;
    }

    public BeanNoField getFieldNoExist() {
        return field;
    }

    public void setFieldNoExist(BeanNoField field) {

    }

}