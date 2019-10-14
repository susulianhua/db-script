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
import org.springframework.util.Assert;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.tinygroup.springmvc.negotiation.impl.HeaderContentNegotiationStrategy;

import java.util.*;

/**
 * 内容协商相关配置管理器
 *
 * @author renhui
 */
public class ContentNegotiationManager implements ContentNegotiationStrategy,
        MediaTypeFileExtensionResolver {

    private static final List<MediaType> MEDIA_TYPE_ALL = Arrays
            .asList(MediaType.ALL);

    private final List<ContentNegotiationStrategy> contentNegotiationStrategies = new ArrayList<ContentNegotiationStrategy>();

    private final Set<MediaTypeFileExtensionResolver> fileExtensionResolvers = new LinkedHashSet<MediaTypeFileExtensionResolver>();

    /**
     * Create an instance with the given ContentNegotiationStrategy instances.
     * <p>
     * Each instance is checked to see if it is also an implementation of
     * MediaTypeFileExtensionResolver, and if so it is registered as such.
     *
     * @param strategies one more more ContentNegotiationStrategy instances
     */
    public ContentNegotiationManager(ContentNegotiationStrategy... strategies) {
        this(Arrays.asList(strategies));
    }

    /**
     * Create an instance with the given ContentNegotiationStrategy instances.
     * <p>
     * Each instance is checked to see if it is also an implementation of
     * MediaTypeFileExtensionResolver, and if so it is registered as such.
     *
     * @param strategies one more more ContentNegotiationStrategy instances
     */
    public ContentNegotiationManager(
            Collection<ContentNegotiationStrategy> strategies) {
        Assert.notEmpty(strategies,
                "At least one ContentNegotiationStrategy is expected");
        this.contentNegotiationStrategies.addAll(strategies);
        for (ContentNegotiationStrategy strategy : this.contentNegotiationStrategies) {
            if (strategy instanceof MediaTypeFileExtensionResolver) {
                this.fileExtensionResolvers
                        .add((MediaTypeFileExtensionResolver) strategy);
            }
        }
    }

    /**
     * Create a default instance with a {@link HeaderContentNegotiationStrategy}
     * .
     */
    public ContentNegotiationManager() {
        this(new HeaderContentNegotiationStrategy());
    }

    /**
     * Add MediaTypeFileExtensionResolver instances.
     * <p>
     * Note that some {@link ContentNegotiationStrategy} implementations also
     * implement {@link MediaTypeFileExtensionResolver} and the class
     * constructor accepting the former will also detect implementations of the
     * latter. Therefore you only need to use this method to register additional
     * instances.
     *
     * @param resolvers one or more resolvers
     */
    public void addFileExtensionResolvers(
            MediaTypeFileExtensionResolver... resolvers) {
        this.fileExtensionResolvers.addAll(Arrays.asList(resolvers));
    }

    /**
     * Delegate to all configured ContentNegotiationStrategy instances until one
     * returns a non-empty list.
     *
     * @param webRequest the current request
     * @return the requested media types or an empty list, never {@code null}
     * @throws HttpMediaTypeNotAcceptableException if the requested media types cannot be parsed
     */
    public List<MediaType> resolveMediaTypes(NativeWebRequest webRequest)
            throws HttpMediaTypeNotAcceptableException {
        for (ContentNegotiationStrategy strategy : this.contentNegotiationStrategies) {
            List<MediaType> mediaTypes = strategy.resolveMediaTypes(webRequest);
            if (mediaTypes.isEmpty() || mediaTypes.equals(MEDIA_TYPE_ALL)) {
                continue;
            }
            return mediaTypes;
        }
        return Collections.emptyList();
    }

    /**
     * Delegate to all configured MediaTypeFileExtensionResolver instances and
     * aggregate the list of all file extensions found.
     */
    public List<String> resolveFileExtensions(MediaType mediaType) {
        Set<String> result = new LinkedHashSet<String>();
        for (MediaTypeFileExtensionResolver resolver : this.fileExtensionResolvers) {
            result.addAll(resolver.resolveFileExtensions(mediaType));
        }
        return new ArrayList<String>(result);
    }

    /**
     * Delegate to all configured MediaTypeFileExtensionResolver instances and
     * aggregate the list of all known file extensions.
     */
    public List<String> getAllFileExtensions() {
        Set<String> result = new LinkedHashSet<String>();
        for (MediaTypeFileExtensionResolver resolver : this.fileExtensionResolvers) {
            result.addAll(resolver.getAllFileExtensions());
        }
        return new ArrayList<String>(result);
    }

}