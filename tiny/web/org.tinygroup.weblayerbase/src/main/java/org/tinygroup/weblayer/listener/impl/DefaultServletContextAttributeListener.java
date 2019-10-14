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
package org.tinygroup.weblayer.listener.impl;

import org.tinygroup.weblayer.listener.TinyServletContextAttributeListener;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

public class DefaultServletContextAttributeListener extends SimpleBasicTinyConfigAware implements
        TinyServletContextAttributeListener {

    private ServletContextAttributeListener servletContextAttributeListener;

    public DefaultServletContextAttributeListener(
            ServletContextAttributeListener servletContextAttributeListener) {
        super();
        this.servletContextAttributeListener = servletContextAttributeListener;
    }

    public void attributeAdded(ServletContextAttributeEvent scab) {
        servletContextAttributeListener.attributeAdded(scab);
    }

    public void attributeRemoved(ServletContextAttributeEvent scab) {
        servletContextAttributeListener.attributeRemoved(scab);
    }

    public void attributeReplaced(ServletContextAttributeEvent scab) {
        servletContextAttributeListener.attributeReplaced(scab);
    }

    public int getOrder() {
        return DEFAULT_PRECEDENCE;
    }

}
