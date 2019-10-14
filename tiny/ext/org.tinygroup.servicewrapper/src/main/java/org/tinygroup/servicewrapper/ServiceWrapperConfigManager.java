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
package org.tinygroup.servicewrapper;

import org.tinygroup.servicewrapper.config.MethodConfig;
import org.tinygroup.servicewrapper.config.MethodConfigs;

import java.lang.reflect.Method;

/**
 * aop缓存配置管理对象
 *
 * @author renhui
 */
public interface ServiceWrapperConfigManager {

    String XSTEAM_PACKAGE_NAME = "servicewrapper";

    void addServiceWrappers(MethodConfigs serviceWrappers);

    void removeServiceWrappers(MethodConfigs serviceWrappers);

    /**
     * 根据方法获取对应的serviceId
     *
     * @param method
     * @return
     */
    String getServiceIdWithMethod(Method method);


    void putServiceWrapper(MethodConfig serviceWrapper);
}
