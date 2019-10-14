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
package org.tinygroup.order.processor;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.List;
import java.util.Map;

/**
 * 排序组的集合
 *
 * @author renhui
 */
public class OrderGroups<T> {

    private static final String DEFAULT_GROUP_NAME = "orderGroup";
    private static final String ORDER_GROUP = "order-group";
    private Map<String, OrderGroup<T>> orderGroups = CollectionUtil.createHashMap();
    private XmlNode root;


    public OrderGroups(XmlNode root) {
        this.root = root;
    }

    public OrderGroup<T> get(String name) {
        return orderGroups.get(name);
    }

    /**
     * 配置加载方法
     */
    public void load() {

        NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(root);
        List<XmlNode> nodes = nameFilter.findNodeList(ORDER_GROUP);
        for (XmlNode xmlNode : nodes) {
            OrderGroup<T> group = new OrderGroup<T>();
            String groupName = xmlNode.getAttribute("name");
            String defaultType = xmlNode.getAttribute("default-type");
            String defaultFeature = xmlNode.getAttribute("default-feature");
            if (defaultType == null || "".equals(defaultType)) {
                defaultType = "after";
            }
            if (defaultFeature == null || "".equals(defaultFeature)) {
                throw new RuntimeException("the order-group node must have default-feature properties");
            }
            group.setDefaultFeatureOrder(defaultType, defaultFeature);
            if (groupName == null) {
                groupName = DEFAULT_GROUP_NAME;
            }
            orderGroups.put(groupName, group);
            group.load(xmlNode);
        }


    }


}
