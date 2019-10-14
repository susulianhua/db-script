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

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.*;
import org.tinygroup.weblayer.config.TinyFilterConfigInfo;
import org.tinygroup.weblayer.configmanager.TinyFilterConfigManager;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * tiny-filter的包装类
 *
 * @author renhui
 */
public class TinyFilterWrapper implements FilterWrapper {

    private static final Logger logger = LoggerFactory
            .getLogger(TinyFilterWrapper.class);
    private Map<String, Filter> filterMap = new LinkedHashMap<String, Filter>();
    private Map<String, String> bean2Config = new HashMap<String, String>();
    private TinyFilterConfigManager tinyFilterConfigManager;
    private TinyFilterManager tinyFilterManager;

    public TinyFilterWrapper(TinyFilterConfigManager tinyFilterConfigManager,
                             TinyFilterManager tinyFilterManager) {
        super();
        this.tinyFilterConfigManager = tinyFilterConfigManager;
        this.tinyFilterManager = tinyFilterManager;
    }

    public void filterWrapper(WebContext context, FilterHandler handler)
            throws IOException, ServletException {
        HttpServletRequest request = context.getRequest();
        HttpServletResponse response = context.getResponse();
        String servletPath = handler.getServletPath();
        List<Filter> matchFilters = getMatchFilters(servletPath, filterMap);
        logger.logMessage(LogLevel.DEBUG,
                "the pre wrapper httpFilters for the request path:[{0}] is [{1}]",
                servletPath, matchFilters);
        TinyFilterChain filterChain = new TinyFilterChain(matchFilters, handler);
        filterChain.doFilter(request, response);
    }

    private List<Filter> getMatchFilters(String servletPath, Map<String, Filter> filterMap) {
        List<Filter> matchFilters = new ArrayList<Filter>();
        for (String filterBeanName : filterMap.keySet()) {
            TinyFilterConfig filterConfig = tinyFilterManager
                    .getTinyFilterConfig(bean2Config.get(filterBeanName));
            if (filterConfig.isMatch(servletPath)) {
                matchFilters.add(filterMap.get(filterBeanName));
            }
        }
        return matchFilters;
    }


    public void addHttpFilter(String filterName, String filterBeanName,
                              Filter filter) {
        filterMap.put(filterBeanName, filter);
        bean2Config.put(filterBeanName, filterName);
    }

    public void init() throws ServletException {
        logger.logMessage(
                LogLevel.DEBUG,
                "TinyFilterWrapper start initialization wrapper httpfilter");
        initWrapperFilters(filterMap);
        logger.logMessage(LogLevel.DEBUG,
                "TinyFilterWrapper initialization end");
    }

    private void initWrapperFilters(Map<String, Filter> filterMap) throws ServletException {
        for (String filterBeanName : filterMap.keySet()) {
            Filter filter = filterMap.get(filterBeanName);
            TinyFilterConfigInfo filterConfigInfo = tinyFilterConfigManager
                    .getFilterConfig(bean2Config.get(filterBeanName));
            filter.init(new TinyWrapperFilterConfig(filterConfigInfo));
        }
    }

    public void destroy() {
        filterMap = null;
    }

}
