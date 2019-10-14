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

import java.util.List;

public abstract class AbstractServiceInfo implements ServiceInfo {
    /**
     *
     */
    private static final long serialVersionUID = -5685351914096465696L;

    public long getRequestTimeout() {
        return 0;
    }

    public int compareTo(ServiceInfo o) {
        int base = o.getServiceId().compareTo(getServiceId());
        if (base != 0) {
            return base;
        }

        int paramBase = compareParameters(o.getParameters(), getParameters());
        if (paramBase != 0) {
            return paramBase;
        }
        int resultBase = compareParameters(o.getResults(), getResults());
        return resultBase;

    }

    private int compareParameters(List<Parameter> oParameters,
                                  List<Parameter> thisParameters) {
        int lengthBase = oParameters.size() - thisParameters.size();
        if (lengthBase != 0) {
            return lengthBase;
        }

        for (int i = 0; i < oParameters.size(); i++) {
            Parameter op = oParameters.get(i);
            Parameter p = thisParameters.get(i);
            int paramBase = op.compareTo(p);
            if (paramBase != 0) {
                return paramBase;
            }
        }
        return 0;
    }
}
