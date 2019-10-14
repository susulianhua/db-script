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
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.tinygroup.springmvc.negotiation.ContentNegotiationStrategy;
import org.tinygroup.springmvc.negotiation.MediaTypeFileExtensionResolver;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A base class for ContentNegotiationStrategy types that maintain a map with keys
 * such as "json" and media types such as "application/json".
 *
 * @author renhui
 */
public abstract class AbstractMappingContentNegotiationStrategy extends MappingTinyMediaTypeFileExtensionResolver
        implements ContentNegotiationStrategy, MediaTypeFileExtensionResolver {


    /**
     * Create an instance with the given extension-to-MediaType lookup.
     *
     * @throws IllegalArgumentException if a media type string cannot be parsed
     */
    public AbstractMappingContentNegotiationStrategy(Map<String, MediaType> mediaTypes) {
        super(mediaTypes);
    }


    public List<MediaType> resolveMediaTypes(NativeWebRequest webRequest) throws HttpMediaTypeNotAcceptableException {
        String key = getMediaTypeKey(webRequest);
        if (StringUtils.hasText(key)) {
            MediaType mediaType = lookupMediaType(key);
            if (mediaType != null) {
                handleMatch(key, mediaType);
                return Collections.singletonList(mediaType);
            }
            mediaType = handleNoMatch(webRequest, key);
            if (mediaType != null) {
                addMapping(key, mediaType);
                return Collections.singletonList(mediaType);
            }
        }
        return Collections.emptyList();
    }

    /**
     * Sub-classes must extract the key to use to look up a media type.
     *
     * @return the lookup key or {@code null} if the key cannot be derived
     */
    protected abstract String getMediaTypeKey(NativeWebRequest request);

    /**
     * Invoked when a matching media type is found in the lookup map.
     */
    protected void handleMatch(String mappingKey, MediaType mediaType) {
    }

    /**
     * Invoked when no matching media type is found in the lookup map.
     * Sub-classes can take further steps to determine the media type.
     */
    protected MediaType handleNoMatch(NativeWebRequest request, String key) throws HttpMediaTypeNotAcceptableException {
        return null;
    }

}