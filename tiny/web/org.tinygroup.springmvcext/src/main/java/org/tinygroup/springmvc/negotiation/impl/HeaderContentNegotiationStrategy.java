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

import java.util.Collections;
import java.util.List;

/**
 * A {@code ContentNegotiationStrategy} that checks the 'Accept' request header.
 */
public class HeaderContentNegotiationStrategy implements ContentNegotiationStrategy {

    /**
     * {@inheritDoc}
     *
     * @throws HttpMediaTypeNotAcceptableException if the 'Accept' header cannot be parsed
     */
    public List<MediaType> resolveMediaTypes(NativeWebRequest request)
            throws HttpMediaTypeNotAcceptableException {

        String header = request.getHeader("Accept");
        if (!StringUtils.hasText(header)) {
            return Collections.emptyList();
        }
        try {
            List<MediaType> mediaTypes = MediaType.parseMediaTypes(header);
            MediaType.sortBySpecificity(mediaTypes);
            MediaType.sortByQualityValue(mediaTypes);
            return mediaTypes;
        } catch (IllegalArgumentException ex) {
            throw new HttpMediaTypeNotAcceptableException(
                    "Could not parse 'Accept' header [" + header + "]: " + ex.getMessage());
        }
    }

}
