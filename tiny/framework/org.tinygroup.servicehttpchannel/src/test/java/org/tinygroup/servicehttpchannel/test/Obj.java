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
package org.tinygroup.servicehttpchannel.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Obj {
    List<Obj2> list2 = new ArrayList<Obj2>();
    List<Obj1> list1 = new ArrayList<Obj1>();
    Map<String, Obj2> map2 = new HashMap<String, Obj2>();
    Obj2[] array2;

    public Obj2[] getArray2() {
        return array2;
    }

    public void setArray2(Obj2[] array2) {
        this.array2 = array2;
    }

    public List<Obj2> getList2() {
        return list2;
    }

    public void setList2(List<Obj2> list2) {
        this.list2 = list2;
    }

    public List<Obj1> getList1() {
        return list1;
    }

    public void setList1(List<Obj1> list1) {
        this.list1 = list1;
    }

    public Map<String, Obj2> getMap2() {
        return map2;
    }

    public void setMap2(Map<String, Obj2> map2) {
        this.map2 = map2;
    }


}
