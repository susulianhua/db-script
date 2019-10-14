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

import java.util.List;

public class SimpleListArrayObject {
    private List<String> nameList;
    private String[] nameArray;
    private Integer[] lengthArray;
    private List<Integer> lengthList;
    private Boolean[] flagArray;
    private List<Boolean> flagList;

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    public String[] getNameArray() {
        return nameArray;
    }

    public void setNameArray(String[] nameArray) {
        this.nameArray = nameArray;
    }

    public Integer[] getLengthArray() {
        return lengthArray;
    }

    public void setLengthArray(Integer[] lengthArray) {
        this.lengthArray = lengthArray;
    }

    public List<Integer> getLengthList() {
        return lengthList;
    }

    public void setLengthList(List<Integer> lengthList) {
        this.lengthList = lengthList;
    }

    public Boolean[] getFlagArray() {
        return flagArray;
    }

    public void setFlagArray(Boolean[] flagArray) {
        this.flagArray = flagArray;
    }

    public List<Boolean> getFlagList() {
        return flagList;
    }

    public void setFlagList(List<Boolean> flagList) {
        this.flagList = flagList;
    }


}
