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
package org.tinygroup.cepcoreimpl.test;

import org.tinygroup.event.AbstractServiceInfo;
import org.tinygroup.event.Parameter;

import java.util.ArrayList;
import java.util.List;

public class ServiceInfoForTest extends AbstractServiceInfo {

    /**
     *
     */
    private static final long serialVersionUID = 6510299323984908078L;
    private String serviceId;
    private List<Parameter> in = new ArrayList<Parameter>();
    private List<Parameter> out = new ArrayList<Parameter>();


    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public List<Parameter> getParameters() {
        return in;
    }

    public List<Parameter> getResults() {
        return out;
    }

    public String getCategory() {
        return null;
    }


}
