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
package org.tinygroup.springmvc.negotiation.impl;

import org.springframework.http.MediaType;
import org.springframework.web.context.request.NativeWebRequest;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springmvc.negotiation.ContentNegotiationStrategy;

import java.util.Collections;
import java.util.List;

/**
 * 返回固定的MediaType
 *
 * @author renhui
 */
public class FixedContentNegotiationStrategy implements ContentNegotiationStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(FixedContentNegotiationStrategy.class);

    private final MediaType defaultContentType;

    /**
     * Create an instance that always returns the given content type.
     */
    public FixedContentNegotiationStrategy(MediaType defaultContentType) {
        this.defaultContentType = defaultContentType;
    }

    public List<MediaType> resolveMediaTypes(NativeWebRequest webRequest) {
        LOGGER.logMessage(LogLevel.DEBUG, "Requested media types is {0} (based on default MediaType)", this.defaultContentType);
        return Collections.singletonList(this.defaultContentType);
    }

}