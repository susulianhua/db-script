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
package org.tinygroup.database.view.impl;

import org.tinygroup.database.config.view.View;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ViewSort implements Comparator<View> {

    private Map<String, List<String>> dependMap;

    public ViewSort(Map<String, List<String>> dependMap) {
        this.dependMap = dependMap;
    }


    public int compare(View view1, View view2) {
        List<String> dependList1 = dependMap.get(view1.getId());
        List<String> dependList2 = dependMap.get(view2.getId());
        boolean contains1 = dependList1.contains(view2.getId());
        boolean contains2 = dependList2.contains(view1.getId());
        if (contains1 && contains2) {
            throw new RuntimeException(String.format(
                    "视图1[id:%s,name:%s]与视图2[id:%s,name:%s]相互依赖", view1.getId(), view1.getName(),
                    view2.getId(), view2.getName()));
        } else if (contains1) {
            return 1;
        } else if (contains2) {
            return -1;
        }
        return 0;
    }

}
