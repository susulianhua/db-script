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
import org.tinygroup.weblayer.TinyFilterConfig;
import org.tinygroup.weblayer.config.FilterMapping;
import org.tinygroup.weblayer.config.TinyFilterConfigInfo;
import org.tinygroup.weblayer.util.RequestURIFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 默认实现
 *
 * @author renhui
 */
public class DefaultTinyFilterConfig extends SimpleBasicTinyConfig implements
        TinyFilterConfig {

    // 存放正则表达式的字符串格式
    private List<String> patternStrs = new ArrayList<String>();

    private List<RequestURIFilter> requestURIFilters = new ArrayList<RequestURIFilter>();

    public DefaultTinyFilterConfig(TinyFilterConfigInfo config) {
        super(config.getConfigName(), config);
        setFilterConfig(config);
    }

    private void setFilterConfig(TinyFilterConfigInfo config) {
        Set<FilterMapping> filterMappings = config.getFilterMappings();
        for (FilterMapping filterMapping : filterMappings) {
            String urlPattern = filterMapping.getUrlPattern();
            if (!patternStrs.contains(urlPattern)) {
                requestURIFilters.add(new RequestURIFilter(urlPattern));
                patternStrs.add(urlPattern);
            }
            logger.logMessage(LogLevel.DEBUG, "<{}>的url-pattern:'{}'",
                    this.configName, urlPattern);
        }
    }

    public boolean isMatch(String url) {
        for (RequestURIFilter requestURIFilter : requestURIFilters) {
            if (requestURIFilter.matches(url, isOrMatchRelation())) {
                logger.logMessage(LogLevel.DEBUG,
                        "请求路径：<{}>,匹配的tiny-filter:<{}>", url, configName);
                return true;
            }
        }
        return false;
    }

}
