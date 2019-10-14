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
package org.tinygroup.springmvc.handlermapping;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.OrderComparator;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.tinygroup.assembly.AssemblyService;
import org.tinygroup.assembly.DefaultAssemblyService;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class TinyHandlerMappingComposite extends ApplicationObjectSupport
        implements HandlerMapping, InitializingBean {
    private static final Logger logger = LoggerFactory
            .getLogger(TinyHandlerMappingComposite.class);
    private List<HandlerMapping> handlerMappingComposite;

    private AssemblyService<HandlerMapping> assemblyService = new DefaultAssemblyService<HandlerMapping>();

    public void setAssemblyService(
            AssemblyService<HandlerMapping> assemblyService) {
        this.assemblyService = assemblyService;
    }

    public void setHandlerMappingComposite(
            List<HandlerMapping> handlerMappingComposite) {
        this.handlerMappingComposite = handlerMappingComposite;
    }

    public void afterPropertiesSet() throws Exception {
        if (CollectionUtil.isEmpty(handlerMappingComposite)) {
            List<HandlerMapping> exclusions = new ArrayList<HandlerMapping>();
            exclusions.add(this.getApplicationContext().getBean(
                    TinyHandlerMapping.class));
            exclusions.add(this);
            assemblyService.setApplicationContext(getApplicationContext());
            assemblyService.setExclusions(exclusions);
            handlerMappingComposite = assemblyService
                    .findParticipants(HandlerMapping.class);
            OrderComparator.sort(this.handlerMappingComposite);
        }
    }

    public HandlerExecutionChain getHandler(HttpServletRequest request)
            throws Exception {
        if (!CollectionUtil.isEmpty(handlerMappingComposite)) {
            for (HandlerMapping handlerMapping : handlerMappingComposite) {
                HandlerExecutionChain handlerExecutionChain = handlerMapping
                        .getHandler(request);
                if (handlerExecutionChain != null) {
                    logger.logMessage(
                            LogLevel.DEBUG,
                            "invoke HandlerMapping.getHandler() method that will proxy [{0}]",
                            handlerExecutionChain);
                    return handlerExecutionChain;
                }
            }
        }
        return null;
    }

}
