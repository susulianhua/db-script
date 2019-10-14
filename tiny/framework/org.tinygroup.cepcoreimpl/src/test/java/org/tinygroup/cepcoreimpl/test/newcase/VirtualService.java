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
package org.tinygroup.cepcoreimpl.test.newcase;

import org.tinygroup.event.Parameter;
import org.tinygroup.event.ServiceInfo;

import java.util.ArrayList;
import java.util.List;

public class VirtualService implements ServiceInfo {
    /**
     *
     */
    private static final long serialVersionUID = 457081027329421381L;
    String serviceId;


    public VirtualService(String serviceId) {
        super();
        this.serviceId = serviceId;
    }


    public int compareTo(ServiceInfo o) {
        return o.getServiceId().compareTo(getServiceId());
    }


    public String getServiceId() {
        return serviceId;
    }

    public List<Parameter> getParameters() {
        return new ArrayList<Parameter>();
    }

    public List<Parameter> getResults() {
        return new ArrayList<Parameter>();
    }

    public String getCategory() {
        return null;
    }


    @Override
    public long getRequestTimeout() {
        return 0;
    }

}
