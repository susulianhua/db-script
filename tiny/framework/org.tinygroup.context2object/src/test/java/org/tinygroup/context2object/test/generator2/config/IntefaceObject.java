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

import org.tinygroup.context2object.test.bean.CatInterface;

import java.util.List;

public class IntefaceObject {
    String name;
    List<CatInterface> cats;
    CatInterface cat;
    CatInterface[] catsArray;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CatInterface> getCats() {
        return cats;
    }

    public void setCats(List<CatInterface> cats) {
        this.cats = cats;
    }

    public CatInterface getCat() {
        return cat;
    }

    public void setCat(CatInterface cat) {
        this.cat = cat;
    }

    public CatInterface[] getCatsArray() {
        return catsArray;
    }

    public void setCatsArray(CatInterface[] catsArray) {
        this.catsArray = catsArray;
    }


}
