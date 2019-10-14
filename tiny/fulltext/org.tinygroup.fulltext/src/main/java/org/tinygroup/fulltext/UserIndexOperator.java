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
package org.tinygroup.fulltext;

/**
 * 带租户的底层操作接口
 *
 * @author yancheng11334
 */
public interface UserIndexOperator extends IndexOperator {

    /**
     * 获取租户ID
     *
     * @return
     */
    String getUserId();

    /**
     * 设置租户ID
     *
     * @param userId
     */
    void setUserId(String userId);

    /**
     * 获取配置ID
     *
     * @return
     */
    String getConfigId();

    /**
     * 设置配置ID
     *
     * @param userId
     */
    void setConfigId(String configId);

    /**
     * 重置
     */
    void reset();
}
