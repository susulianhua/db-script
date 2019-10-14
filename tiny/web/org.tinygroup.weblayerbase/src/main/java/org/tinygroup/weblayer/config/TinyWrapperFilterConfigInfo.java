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

@XStreamAlias("tiny-wrapper-filter")
public class TinyWrapperFilterConfigInfo extends TinyFilterConfigInfo {

    private static String FILTER_BEAN_NAME = "filter_beans";

    private static String POST_FILTER_BEAN_NAME = "post_filter_beans";

    public String getFilterBeanName() {
        String filterBean = getParameterMap().get(FILTER_BEAN_NAME);
        return filterBean;
    }

    public String getPostFilterBeanName() {
        String filterBean = getParameterMap().get(POST_FILTER_BEAN_NAME);
        return filterBean;
    }

}
