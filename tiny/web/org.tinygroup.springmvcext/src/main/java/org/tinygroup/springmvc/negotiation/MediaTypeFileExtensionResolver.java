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

import java.util.List;

/**
 * 从MediaType获取文件扩展类型
 *
 * @author renhui
 */
public interface MediaTypeFileExtensionResolver {

    /**
     * Resolve the given media type to a list of path extensions.
     *
     * @param mediaType the media type to resolve
     * @return a list of extensions or an empty list (never {@code null})
     */
    List<String> resolveFileExtensions(MediaType mediaType);

    /**
     * Return all registered file extensions.
     *
     * @return a list of extensions or an empty list (never {@code null})
     */
    List<String> getAllFileExtensions();

}