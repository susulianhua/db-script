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
package org.tinygroup.tinydb.sql;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.tinydb.sql.condition.ConditionGenerater;
import org.tinygroup.tinydb.sql.condition.impl.EqualsConditionGenerater;
import org.tinygroup.tinydb.sql.group.GroupGenerater;
import org.tinygroup.tinydb.sql.group.impl.DefaultGroupGenerater;
import org.tinygroup.tinydb.sql.order.OrderGenerater;
import org.tinygroup.tinydb.sql.order.impl.AscOrderGenerater;
import org.tinygroup.tinydb.sql.order.impl.DescOrderGenerater;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 存放sql生成片段的实例的容器，例如条件、分组、排序的片段
 *
 * @author renhui
 */
public class SQLGenerateContainer {

    private Map<String, ConditionGenerater> conditionModeMap = new HashMap<String, ConditionGenerater>();

    private Map<String, OrderGenerater> orderModeMap = new HashMap<String, OrderGenerater>();

    private GroupGenerater generater = new DefaultGroupGenerater();

    public void initContainer() {
        Collection<ConditionGenerater> conditionModes = BeanContainerFactory
                .getBeanContainer(this.getClass().getClassLoader()).getBeans(
                        ConditionGenerater.class);
        if (!CollectionUtil.isEmpty(conditionModes)) {
            for (ConditionGenerater conditionMode : conditionModes) {
                conditionModeMap.put(conditionMode.getConditionMode(),
                        conditionMode);
            }
        } else {// 默认注册equals的条件表达式
            ConditionGenerater equasl = new EqualsConditionGenerater();
            conditionModeMap.put(equasl.getConditionMode(), equasl);
        }

        Collection<OrderGenerater> orderModes = BeanContainerFactory
                .getBeanContainer(this.getClass().getClassLoader()).getBeans(
                        OrderGenerater.class);
        if (!CollectionUtil.isEmpty(orderModes)) {
            for (OrderGenerater orderGenerater : orderModes) {
                orderModeMap.put(orderGenerater.getOrderMode(), orderGenerater);
            }
        } else {// 注册asc与desc
            OrderGenerater asc = new AscOrderGenerater();
            orderModeMap.put(asc.getOrderMode(), asc);
            DescOrderGenerater desc = new DescOrderGenerater();
            orderModeMap.put(desc.getOrderMode(), desc);
        }

    }

    public ConditionGenerater getConditionGenerater(String conditionMode) {
        return conditionModeMap.get(conditionMode);
    }

    public OrderGenerater getOrderGenerater(String orderMode) {
        return orderModeMap.get(orderMode);
    }

    public GroupGenerater getGroupGenerater() {
        return generater;
    }
}
