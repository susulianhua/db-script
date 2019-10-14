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
package org.tinygroup.weblayer2;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求的枚举，与 org.springframework.http。HttpMethod相同
 * @author renhui
 *
 */
public enum HttpMethod {

    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;


    private static final Map<String, HttpMethod> mappings = new HashMap<String, HttpMethod>(8);

    static {
        for (HttpMethod httpMethod : values()) {
            mappings.put(httpMethod.name(), httpMethod);
        }
    }


    /**
     * Resolve the given method value to an {@code HttpMethod}.
     * @param method the method value as a String
     * @return the corresponding {@code HttpMethod}, or {@code null} if not found
     */
    public static HttpMethod resolve(String method) {
        return (method != null ? mappings.get(method) : null);
    }


    /**
     * Determine whether this {@code HttpMethod} matches the given
     * method value.
     * @param method the method value as a String
     * @return {@code true} if it matches, {@code false} otherwise
     */
    public boolean matches(String method) {
        return (this == resolve(method));
    }

}