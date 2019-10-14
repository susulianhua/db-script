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
package org.tinygroup.flow.test.testcase.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;

import java.util.List;

public class ListComponent implements ComponentInterface {
    public List<String> list;
    public String str;
    public int ii;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getIi() {
        return ii;
    }

    public void setIi(int ii) {
        this.ii = ii;
    }

    public void execute(Context context) {
        context.put("ii1", ii);
        context.put("str1", str);
        context.put("list1", list);
    }

}	
