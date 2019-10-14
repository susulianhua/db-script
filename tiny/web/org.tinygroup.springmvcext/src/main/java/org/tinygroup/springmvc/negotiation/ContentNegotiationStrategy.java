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
package org.tinygroup.springmvc.negotiation;

import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

/**
 * 从请求中获取MediaType的策略接口
 *
 * @author renhui
 */
public interface ContentNegotiationStrategy {

    /**
     * Resolve the given request to a list of media types. The returned list is
     * ordered by specificity first and by quality parameter second.
     *
     * @param webRequest the current request
     * @return the requested media types or an empty list (never {@code null})
     * @throws HttpMediaTypeNotAcceptableException if the requested media
     *                                             types cannot be parsed
     */
    List<MediaType> resolveMediaTypes(NativeWebRequest webRequest)
            throws HttpMediaTypeNotAcceptableException;

}
