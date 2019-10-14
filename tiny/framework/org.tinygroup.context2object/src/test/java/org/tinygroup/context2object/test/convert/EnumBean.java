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
package org.tinygroup.context2object.test.convert;

import java.util.List;

public class EnumBean {
    EnumObject[] array;
    List<EnumObject> list;
    List<EnumBeanSimple> simpleList;
    EnumBeanSimple[] simpleArray;

    public EnumObject[] getArray() {
        return array;
    }

    public void setArray(EnumObject[] array) {
        this.array = array;
    }

    public List<EnumObject> getList() {
        return list;
    }

    public void setList(List<EnumObject> list) {
        this.list = list;
    }

    public List<EnumBeanSimple> getSimpleList() {
        return simpleList;
    }

    public void setSimpleList(List<EnumBeanSimple> simpleList) {
        this.simpleList = simpleList;
    }

    public EnumBeanSimple[] getSimpleArray() {
        return simpleArray;
    }

    public void setSimpleArray(EnumBeanSimple[] simpleArray) {
        this.simpleArray = simpleArray;
    }

}
