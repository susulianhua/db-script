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
package org.tinygroup.flow.test.testcase;

import org.tinygroup.event.AbstractServiceInfo;
import org.tinygroup.event.Parameter;
import org.tinygroup.flow.config.Flow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoguo on 14-4-9.
 */
public class FlowServiceInfoForTest extends AbstractServiceInfo {
    private static final long serialVersionUID = -7451038800051026910L;
    private String serviceId;
    private List<Parameter> parameters;
    private List<Parameter> results;
    private String category;

    public FlowServiceInfoForTest(Flow flow) {
        serviceId = flow.getId();
        parameters = flow.getParameters();
        results = flow.getOutputParameters();
        category = flow.getCategory();
    }

    public String getServiceId() {
        return serviceId;
    }

    public List<Parameter> getParameters() {
        if (parameters == null) {
            parameters = new ArrayList<Parameter>();
        }
        return parameters;
    }

    public List<Parameter> getResults() {
        if (results == null) {
            results = new ArrayList<Parameter>();
        }
        return results;
    }

    public String getCategory() {
        return category;
    }

}
