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
package org.tinygroup.aopcache;

import org.tinygroup.aopcache.config.AopCaches;
import org.tinygroup.aopcache.config.CacheAction;

import java.lang.reflect.Method;
import java.util.List;

/**
 * aop缓存配置管理对象
 *
 * @author renhui
 */
public interface AopCacheConfigManager {

    String XSTEAM_PACKAGE_NAME = "aopcache";

    void addAopCaches(AopCaches aopCaches);

    void removeAopCaches(AopCaches aopCaches);

    /**
     * 根据方法获取其关联的缓存aop操作配置
     *
     * @param method
     * @return
     */
    List<CacheAction> getActionsWithMethod(Method method);


}
