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
package org.tinygroup.event;

import java.util.ArrayList;
import java.util.List;

public class BaseServiceInfo extends AbstractServiceInfo {

    private static final long serialVersionUID = -6526088933543577083L;
    /**
     * 服务标识，唯一确定一个服务，如果重复，只有组织标识，模块标识，名称全部相同，且版本不同，才可以注册
     */
    private String serviceId;
    /**
     * 输入参数描述列表
     */
    private List<Parameter> parameters;
    /**
     * 输出参数描述列表
     */
    private List<Parameter> results;

    private String category;

    /**
     * 服务自定义超时时间
     */
    private long requestTimeout;

    public BaseServiceInfo(ServiceInfo info) {
        this.category = info.getCategory();
        this.serviceId = info.getServiceId();
        this.parameters = info.getParameters();
        this.results = info.getResults();
        this.requestTimeout = info.getRequestTimeout();
    }

    public List<Parameter> getParameters() {
        if (parameters == null) {
            parameters = new ArrayList<Parameter>();
        }
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<Parameter> getResults() {
        if (results == null) {
            results = new ArrayList<Parameter>();
        }
        return results;
    }

    public void setResults(List<Parameter> results) {
        this.results = results;
    }

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

    public long getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(long requestTimeout) {
        this.requestTimeout = requestTimeout;
    }
}
