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
import org.springframework.util.Assert;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.Map;

/**
 * A ContentNegotiationStrategy that uses a request parameter to determine what
 * media types are requested. The default parameter name is {@code format}. Its
 * value is used to look up the media type in the map given to the constructor.
 *
 * @author renhui
 */
public class ParameterContentNegotiationStrategy extends
        AbstractMappingContentNegotiationStrategy {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ParameterContentNegotiationStrategy.class);

    private String parameterName = "format";

    /**
     * Create an instance with the given extension-to-MediaType lookup.
     *
     * @throws IllegalArgumentException if a media type string cannot be parsed
     */
    public ParameterContentNegotiationStrategy(Map<String, MediaType> mediaTypes) {
        super(mediaTypes);
    }

    /**
     * Set the parameter name that can be used to determine the requested media
     * type.
     * <p>
     * The default parameter name is {@code format}.
     */
    public void setParameterName(String parameterName) {
        Assert.notNull(parameterName, "parameterName is required");
        this.parameterName = parameterName;
    }

    @Override
    protected String getMediaTypeKey(NativeWebRequest webRequest) {
        return webRequest.getParameter(this.parameterName);
    }

    @Override
    protected void handleMatch(String mediaTypeKey, MediaType mediaType) {
        LOGGER.logMessage(
                LogLevel.DEBUG,
                "Requested media type is '{0}' (based on parameter '{1}'='{2}')",
                mediaType, this.parameterName, mediaTypeKey);
    }

    @Override
    protected MediaType handleNoMatch(NativeWebRequest request, String key)
            throws HttpMediaTypeNotAcceptableException {
        throw new HttpMediaTypeNotAcceptableException(getAllMediaTypes());
    }
}
