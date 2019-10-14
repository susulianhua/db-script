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
package org.tinygroup.springmvc.util;

import org.springframework.http.MediaType;
import org.tinygroup.commons.tools.StringUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public final class MediaTypeUtil {

    private static final String PARAM_QUALITY_FACTOR = "q";

    /**
     * Return a replica of this instance with the quality value of the given MediaType.
     *
     * @return the same instance if the given MediaType doesn't have a quality value, or a new one otherwise
     */
    public static MediaType copyQualityValue(MediaType acceptType, MediaType produceType) {
        String parameter = acceptType.getParameter(PARAM_QUALITY_FACTOR);
        if (StringUtil.isBlank(parameter)) {
            return produceType;
        }
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put(PARAM_QUALITY_FACTOR, parameter);
        return new MediaType(produceType, params);
    }

}
