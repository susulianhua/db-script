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
package org.tinygroup.cepcore.aop.impl;

import org.tinygroup.cepcore.aop.CEPCoreAopAdapter;
import org.tinygroup.event.Event;

import java.util.regex.Pattern;

public class CEPCoreAopAdapterContainer {

    private CEPCoreAopAdapter adapter;
    private Pattern servicePattern = null;

    public CEPCoreAopAdapterContainer(CEPCoreAopAdapter adapter,
                                      Pattern serviceIdPattern) {
        this.adapter = adapter;
        this.servicePattern = serviceIdPattern;
    }

    public void handle(Event e) {
        if (servicePattern == null) {
            adapter.handle(e);
            return;
        }
        String requestId = e.getServiceRequest().getServiceId();
        if (servicePattern.matcher(requestId).matches()) {
            adapter.handle(e);
        }
    }
}
