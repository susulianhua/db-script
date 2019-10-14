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
package org.tinygroup.serviceweblayer.test.service;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class School implements Serializable {

    private String str;
    private List<String> listStr;
    private Grade grade;
    private List<Grade> gradeList;
    private Grade[] gradeArray;

    public Grade[] getGradeArray() {
        return gradeArray;
    }

    public void setGradeArray(Grade[] gradeArray) {
        this.gradeArray = gradeArray;
    }

    public List<Grade> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public List<String> getListStr() {
        return listStr;
    }

    public void setListStr(List<String> listStr) {
        this.listStr = listStr;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

}
