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

import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;

import java.util.List;

/**
 * 事件处理器 ，每个事件处理器，都需要提供其ID，路由表及处理器
 *
 * @author luoguo
 */
public interface EventProcessor {
    int TYPE_REMOTE = 1;
    int TYPE_LOCAL = 2;

    /**
     * 判断当前处理器是否能处理对应的节点
     * @param nodeString
     * @return
     */
    boolean canProcess(String nodeString);
    /**
     * 处理事件
     *
     * @param event
     */
    void process(Event event);

    /**
     * 设置CEPCore
     *
     * @param cepCore
     */
    void setCepCore(CEPCore cepCore);

    /**
     * 返回当前处理器中的所有服务
     *
     * @return
     */
    List<ServiceInfo> getServiceInfos();

    /**
     * 返回处理器ID，ID必须唯一
     *
     * @return
     */
    String getId();

    /**
     * 返回处理器类型
     *
     * @return
     */
    int getType();

    /**
     * 返回处理权重
     *
     * @return
     */
    int getWeight();

    /**
     * 返回正则
     *
     * @return
     */
    List<String> getRegex();

    /**
     * 获取是否已被读取状态
     *
     * @return
     */
    boolean isRead();

    /**
     * 设置读取状态，当刷新时，将此状态设置为false,当注册完成后，设置为true
     *
     * @param read
     */
    void setRead(boolean read);


    /**
     * 获取是否是enable，如果为false，则不接收任何服务处理
     *
     * @return
     */
    boolean isEnable();

    /**
     * 设置enable
     *
     * @param enable
     */
    void setEnable(boolean enable);
}
