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

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcore.impl.AbstractEventProcessor;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class EventProcessorForTestEnable extends AbstractEventProcessor {
    private static Logger LOGGER = LoggerFactory
            .getLogger(EventProcessorForTestEnable.class);
    List<ServiceInfo> list = new ArrayList<ServiceInfo>();
    private boolean enable = true;

    public void process(Event event) {
        String serviceId = event.getServiceRequest().getServiceId();
        LOGGER.logMessage(LogLevel.INFO, "执行服务:{}", serviceId);
    }

    public void setCepCore(CEPCore cepCore) {

    }

    public List<ServiceInfo> getServiceInfos() {
        return list;
    }

    public String getId() {
        return EventProcessorForTestEnable.class.getSimpleName();
    }

    public int getType() {
        return 2;
    }

    public int getWeight() {
        return 0;
    }

    public List<String> getRegex() {
        return null;
    }

    public boolean isRead() {
        return false;
    }

    public void setRead(boolean read) {

    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

}
