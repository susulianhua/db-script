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
package org.tinygroup.flow.test.newtestcase.simpleflow.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;

import java.util.List;

/**
 * 用于测试流程编排基础功能的组件
 *
 * @author zhangliang08072
 * @version $Id: SimpleFlowComponent.java, v 0.1 2016年4月28日 上午9:37:55 zhangliang08072 Exp $
 */
public class SimpleFlowForeachComponent implements ComponentInterface {

    public void execute(Context context) {

        //此部分用于测试遍历
        Integer listscount = context.get("listscount");
        Integer listsum = context.get("listsum");
        if (listsum == null) {
            listsum = 0;
        }
        List<Integer> lists = context.get("lists");
        listsum += lists.get(listscount);
        listscount++;
        context.put("listscount", listscount);
        context.put("listsum", listsum);

    }
}
