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
package org.tinygroup.weblayer.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.LinkedHashSet;
import java.util.Set;

@XStreamAlias("tiny-filter")
public class TinyFilterConfigInfo extends BasicConfigInfo {

    @XStreamImplicit
    private Set<FilterMapping> filterMappings;

    public Set<FilterMapping> getFilterMappings() {
        if (filterMappings == null) {
            filterMappings = new LinkedHashSet<FilterMapping>();
        }
        return filterMappings;
    }

    public void setFilterMappings(Set<FilterMapping> filterMappings) {
        this.filterMappings = filterMappings;
    }

    public void combine(TinyFilterConfigInfo configInfo) {
        getParameterMap().putAll(configInfo.getParameterMap());
        getFilterMappings().addAll(configInfo.getFilterMappings());
    }

}
