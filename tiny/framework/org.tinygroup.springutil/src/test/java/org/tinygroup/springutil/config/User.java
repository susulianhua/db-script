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
package org.tinygroup.springutil.config;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class User {
    String name;
    int age;
    Cat cat;
    List<Cat> cats;
    Set<Cat> catSet;
    Map<String, Cat> catMap;

    public Set<Cat> getCatSet() {
        return catSet;
    }

    public void setCatSet(Set<Cat> catSet) {
        this.catSet = catSet;
    }

    public Map<String, Cat> getCatMap() {
        return catMap;
    }

    public void setCatMap(Map<String, Cat> catMap) {
        this.catMap = catMap;
    }

    public List<Cat> getCats() {
        return cats;
    }

    public void setCats(List<Cat> cats) {
        this.cats = cats;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
