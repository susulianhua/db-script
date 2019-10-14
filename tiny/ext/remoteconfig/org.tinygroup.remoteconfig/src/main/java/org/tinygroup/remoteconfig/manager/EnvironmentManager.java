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
package org.tinygroup.remoteconfig.manager;

import org.tinygroup.remoteconfig.config.Environment;

import java.util.List;

public interface EnvironmentManager {
    Environment add(Environment env, String versionId, String productId);

    void update(Environment env, String versionId, String productId);

//	void delete(String envId, String versionId, String productId);

    Environment get(String envId, String versionId, String productId);

    /**
     * 添加一个默认环境
     *
     * @param env
     * @return
     */
    Environment add(Environment env);

    /**
     * 修改一个默认环境
     *
     * @param env
     * @return
     */
    void update(Environment env);

    /**
     * 删除一个默认环境
     *
     * @param env
     * @return
     */
    void delete(String envId);

    /**
     * 查询一个环境
     *
     * @param env
     * @return
     */
    Environment get(String envId);

    /**
     * 移联环境和指定产品版本的关联
     *
     * @param env
     * @return
     */
    void delete(String envId, String versionId, String productId);

    /**
     * 批量查询，如果项目ID或者版本为空的情况下，返回默认环境
     *
     * @param versionId
     * @param productId
     * @return
     */
    List<Environment> query(String versionId, String productId);


}
