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
package org.tinygroup.servicewrapper.namediscoverer;

import org.springframework.core.ParameterNameDiscoverer;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.event.Parameter;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.servicewrapper.ServiceIdAnaly;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 通过服务号获取对应方法的参数名称
 *
 * @author renhui
 */
public class ServiceParameterNameDiscoverer implements ParameterNameDiscoverer {
    private ServiceIdAnaly serviceIdAnaly;

    private CEPCore core;

    public CEPCore getCore() {
        return core;
    }

    public void setCore(CEPCore core) {
        this.core = core;
    }


    public ServiceIdAnaly getServiceIdAnaly() {
        return serviceIdAnaly;
    }

    public void setServiceIdAnaly(ServiceIdAnaly serviceIdAnaly) {
        this.serviceIdAnaly = serviceIdAnaly;
    }

    public String[] getParameterNames(Method method) {
        String serviceId = serviceIdAnaly.analyMethod(method);
        ServiceInfo serviceInfo = core.getServiceInfo(serviceId);
        List<Parameter> params = serviceInfo.getParameters();
        if (!CollectionUtil.isEmpty(params)) {
            String[] parameterNames = new String[params.size()];
            for (int i = 0; i < parameterNames.length; i++) {
                parameterNames[i] = params.get(i).getName();
            }
            return parameterNames;
        }
        return null;
    }

    public String[] getParameterNames(Constructor ctor) {
        return null;
    }

}
