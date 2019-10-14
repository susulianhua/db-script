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
package org.tinygroup.mbean.config;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.List;

public abstract class MBeanFileConfig extends AbstractFileProcessor {
    protected static final String MBEAN_XSTREAM_PACKAGENAME = "mbean";
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MBeanFileConfig.class);

    public void loadMBeanServer(ClassLoader classLoader) throws Exception {
        List<TinyModeInfo> list = getTinyModeInfos();
        if (list.isEmpty()) {
            return;
        }
        MBeanServer mb = ManagementFactory.getPlatformMBeanServer();
        for (TinyModeInfo info : list) {
            ObjectName name = new ObjectName("TinyMBean:name="
                    + info.getTinyModeName());
            Object object = null;
            if ("bean".equals(info.getFrom())) {
                object = BeanContainerFactory.getBeanContainer(classLoader).getBean(info.getValue());
            } else {
                object = Class.forName(info.getValue()).newInstance();
            }
            mb.registerMBean(object, name);
            LOGGER.logMessage(LogLevel.INFO, "MBean服务[{0}]注册成功",
                    info.getTinyModeName());
        }
    }

    protected abstract List<TinyModeInfo> getTinyModeInfos();

}
