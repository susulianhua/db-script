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
package org.tinygroup.remoteconfig;

import org.tinygroup.remoteconfig.manager.ConfigItemReader;


/**
 * 这个类是客户端工程使用(具体使用远程配置的工程) 所以只有只读方法
 *
 * @author chenjiao
 */
public interface RemoteConfigReadClient extends ConfigItemReader,RemoteConfigPublishWatch {

    /**
     * 客户端初始化
     */
    public void start();

    /**
     * 客户端停止
     */
    public void stop();

}
