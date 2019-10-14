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

import org.tinygroup.remoteconfig.config.ConfigPath;
import org.tinygroup.remoteconfig.config.Module;

import java.util.List;

public interface ModuleManager {
    /**
     * 添加一个module，若entity的中moduleid为空，则直接在环境下添加module，否则作为module的子module
     *
     * @param module
     * @param entity
     * @return
     */
    Module add(Module module, ConfigPath entity);

    /**
     * 更新一个module
     *
     * @param module
     * @param entity
     */
    void update(Module module, ConfigPath entity);

    /**
     * 删除一个module
     *
     * @param entity
     */
    void delete(ConfigPath entity);

    /**
     * 查询一个module
     *
     * @param entity
     * @return
     */
    Module get(ConfigPath entity);


    /**
     * 查询一个module下所有的子module
     *
     * @param entity
     * @return
     */
    public List<Module> querySubModules(ConfigPath entity);
}
