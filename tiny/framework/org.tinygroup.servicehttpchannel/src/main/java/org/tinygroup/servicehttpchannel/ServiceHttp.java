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
package org.tinygroup.servicehttpchannel;

import org.tinygroup.event.AbstractServiceInfo;
import org.tinygroup.event.Parameter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ServiceHttp extends AbstractServiceInfo {
    /**
     *
     */
    private static final long serialVersionUID = 4345831876683679140L;
    transient Type resultType;
    transient Class<?> type;
    transient String methodName;
    private String serviceId;
    private String category;
    private List<Parameter> parameters = new ArrayList<Parameter>();
    private List<Parameter> results = new ArrayList<Parameter>();

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<Parameter> getResults() {
        return results;
    }

    public void setResults(List<Parameter> results) {
        this.results = results;
    }


    public void setType(Class<?> type) {
        this.type = type;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public String getResultName() {
        if (results.size() == 0) {
            return null;
        }
        return results.get(0).getName();
    }

}
