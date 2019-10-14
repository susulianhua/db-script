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
package org.tinygroup.assembly;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.tinygroup.commons.tools.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultAssemblyService<T> extends ApplicationObjectSupport implements AssemblyService<T> {

    private List<T> exclusions = new ArrayList<T>();

    public void setExclusions(List<T> exclusions) {
        this.exclusions = exclusions;
    }

    public List<T> findParticipants(Class<T> requiredType) {
        Map<String, T> map = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                this.getListableBeanFactory(), requiredType);
        if (map.isEmpty()) {
            return null;
        }
        List<T> list = new ArrayList<T>(map.size());
        list.addAll(map.values());
        if (!CollectionUtil.isEmpty(exclusions)) {
            for (T exclusion : exclusions) {
                list.remove(exclusion);
            }
        }
        return list;
    }

    private ListableBeanFactory getListableBeanFactory() {
        if (getApplicationContext() instanceof ConfigurableApplicationContext) {
            return ((ConfigurableApplicationContext) getApplicationContext())
                    .getBeanFactory();
        }
        return getApplicationContext();
    }

}
