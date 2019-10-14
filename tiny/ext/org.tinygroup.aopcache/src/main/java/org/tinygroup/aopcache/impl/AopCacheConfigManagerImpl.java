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
package org.tinygroup.aopcache.impl;

import org.tinygroup.aopcache.AopCacheConfigManager;
import org.tinygroup.aopcache.base.MethodDescription;
import org.tinygroup.aopcache.config.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * aop缓存配置管理器
 *
 * @author renhui
 */
public class AopCacheConfigManagerImpl implements AopCacheConfigManager {

    private Map<MethodDescription, CacheActions> methodActionMap = new HashMap<MethodDescription, CacheActions>();

    public void addAopCaches(AopCaches aopCaches) {
        List<AopCache> cacheConfigs = aopCaches.getCacheConfigs();
        for (AopCache aopCache : cacheConfigs) {
            List<MethodConfig> methodConfigs = aopCache.getMethodConfigs();
            for (MethodConfig methodConfig : methodConfigs) {
                MethodDescription description = new MethodDescription();
                description.setClassName(aopCache.getClassName());
                description.setMethodName(methodConfig.getMethodName());
                List<ParameterType> paramTypes = methodConfig.getParamTypes();
                StringBuilder typeBuilder = new StringBuilder();
                for (int i = 0; i < paramTypes.size(); i++) {
                    typeBuilder.append(paramTypes.get(i).getType());
                    if (i < paramTypes.size() - 1) {
                        typeBuilder.append(";");
                    }
                }
                description.setParameterTypes(typeBuilder.toString());
                methodActionMap
                        .put(description, methodConfig.getCacheActions());
            }
        }
    }

    public void removeAopCaches(AopCaches aopCaches) {
        List<AopCache> cacheConfigs = aopCaches.getCacheConfigs();
        for (AopCache aopCache : cacheConfigs) {
            List<MethodConfig> methodConfigs = aopCache.getMethodConfigs();
            for (MethodConfig methodConfig : methodConfigs) {
                MethodDescription description = new MethodDescription();
                description.setClassName(aopCache.getClassName());
                description.setMethodName(methodConfig.getMethodName());
                List<ParameterType> paramTypes = methodConfig.getParamTypes();
                StringBuilder typeBuilder = new StringBuilder();
                for (int i = 0; i < paramTypes.size(); i++) {
                    typeBuilder.append(paramTypes.get(i).getType());
                    if (i < paramTypes.size() - 1) {
                        typeBuilder.append(";");
                    }
                }
                description.setParameterTypes(typeBuilder.toString());
                methodActionMap.remove(description);
            }
        }

    }

    public List<CacheAction> getActionsWithMethod(Method method) {
        MethodDescription description = MethodDescription
                .createMethodDescription(method);
        CacheActions cacheActions = methodActionMap.get(description);
        if (cacheActions != null) {
            return cacheActions.getActions();
        }
        return null;
    }

}
