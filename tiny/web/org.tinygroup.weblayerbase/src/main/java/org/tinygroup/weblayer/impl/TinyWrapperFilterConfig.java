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
package org.tinygroup.weblayer.impl;

import org.tinygroup.commons.tools.Enumerator;
import org.tinygroup.weblayer.config.TinyFilterConfigInfo;
import org.tinygroup.weblayer.listener.ServletContextHolder;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * FilterConfig的tiny实现
 *
 * @author renhui
 */
public class TinyWrapperFilterConfig implements FilterConfig {

    public static final String FILTER_BEAN_NAMES = "filter_beans";

    private static final String WRAPPER_FILTER_NAME = "wrapper_filter";

    private TinyFilterConfigInfo tinyFilterConfigInfo;

    public TinyWrapperFilterConfig(TinyFilterConfigInfo tinyFilterConfigInfo) {
        this.tinyFilterConfigInfo = tinyFilterConfigInfo;
    }

    public String getFilterName() {
        return WRAPPER_FILTER_NAME;
    }

    public ServletContext getServletContext() {
        return ServletContextHolder.getServletContext();
    }

    public String getInitParameter(String name) {
        return tinyFilterConfigInfo.getParameterValue(name);
    }

    public Enumeration getInitParameterNames() {
        return new Enumerator(tinyFilterConfigInfo.getIterator());
    }

}
