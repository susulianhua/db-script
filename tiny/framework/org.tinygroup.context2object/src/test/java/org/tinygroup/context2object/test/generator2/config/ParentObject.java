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
package org.tinygroup.context2object.test.generator2.config;

import org.tinygroup.context2object.test.convert.EnumObject;

import java.util.List;

public class ParentObject {
    String pro;
    SimpleObject simpleObject;
    SimpleObject[] simpleObjectArray;
    List<SimpleObject> simpleObjectList;
    List<EnumObject> enumObjectList;
    EnumObject[] enumObjectArray;
    EnumObject enumObject;
    List<Integer> lengths;

    public List<Integer> getLengths() {
        return lengths;
    }

    public void setLengths(List<Integer> lengths) {
        this.lengths = lengths;
    }

    public SimpleObject[] getSimpleObjectArray() {
        return simpleObjectArray;
    }

    public void setSimpleObjectArray(SimpleObject[] simpleObjectArray) {
        this.simpleObjectArray = simpleObjectArray;
    }

    public List<SimpleObject> getSimpleObjectList() {
        return simpleObjectList;
    }

    public void setSimpleObjectList(List<SimpleObject> simpleObjectList) {
        this.simpleObjectList = simpleObjectList;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public SimpleObject getSimpleObject() {
        return simpleObject;
    }

    public void setSimpleObject(SimpleObject simpleObject) {
        this.simpleObject = simpleObject;
    }

    public List<EnumObject> getEnumObjectList() {
        return enumObjectList;
    }

    public void setEnumObjectList(List<EnumObject> enumObjectList) {
        this.enumObjectList = enumObjectList;
    }

    public EnumObject[] getEnumObjectArray() {
        return enumObjectArray;
    }

    public void setEnumObjectArray(EnumObject[] enumObjectArray) {
        this.enumObjectArray = enumObjectArray;
    }

    public EnumObject getEnumObject() {
        return enumObject;
    }

    public void setEnumObject(EnumObject enumObject) {
        this.enumObject = enumObject;
    }

}
