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
package org.tinygroup.springmvc.handler;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.tinygroup.assembly.AssemblyService;
import org.tinygroup.assembly.DefaultAssemblyService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author renhui
 */
public class MethodInvokePreHandlerComposite extends ApplicationObjectSupport
        implements InitializingBean {

    private List<MethodInvokePreHandler> methodInvokePreHandlers;

    private AssemblyService<MethodInvokePreHandler> assemblyService = new DefaultAssemblyService<MethodInvokePreHandler>();

    public AssemblyService<MethodInvokePreHandler> getAssemblyService() {
        return assemblyService;
    }

    public void setAssemblyService(
            AssemblyService<MethodInvokePreHandler> assemblyService) {
        this.assemblyService = assemblyService;
    }


    public List<MethodInvokePreHandler> getMethodInvokePreHandlers() {
        return methodInvokePreHandlers;
    }

    public void afterPropertiesSet() throws Exception {
        assemblyService.setApplicationContext(getApplicationContext());
        List<MethodInvokePreHandler> preHandlers = assemblyService
                .findParticipants(MethodInvokePreHandler.class);
        if (preHandlers == null) {
            preHandlers = new ArrayList<MethodInvokePreHandler>();
        }
        this.methodInvokePreHandlers = preHandlers;
    }

}
