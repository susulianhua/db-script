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
package org.tinygroup.cepcore;

import org.tinygroup.xmlparser.node.XmlNode;

import java.util.List;

public interface CEPCoreOperator {
    int TYPE_SC = 1;
    int TYPE_NODE = 2;

    /**
     * 启动节点
     *
     * @param cep
     */
    void startCEPCore(CEPCore cep);

    /**
     * 停止节点
     *
     * @param cep
     */
    void stopCEPCore(CEPCore cep);

    /**
     * 设置CEPCore
     *
     * @param cep
     */
    void setCEPCore(CEPCore cep);

    /**
     * 设置参数
     *
     * @param node
     */
    void setParam(XmlNode node);

    /**
     * 查询与SC之间的链接情况的状态
     *
     * @return 结果状态列表, 单条状态数据为SCIp:SCPort:status,status为ture或者false
     * Node Operator返回的为List<String>
     * SC   Operator返回为null
     */
    List<String> getConnectStatus();

}
