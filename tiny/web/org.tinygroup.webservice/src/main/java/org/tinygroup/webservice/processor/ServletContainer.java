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
package org.tinygroup.webservice.processor;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.ResourceLoader;
import com.sun.xml.ws.api.server.BoundEndpoint;
import com.sun.xml.ws.api.server.Container;
import com.sun.xml.ws.transport.http.servlet.ServletModule;

import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to {@link ServletContext} via {@link Container}. Pipes
 * can get ServletContext from Container and use it to load some resources.
 */
class ServletContainer extends Container {
    private final ServletContext servletContext;

    private final ServletModule module = new ServletModule() {
        private final List<BoundEndpoint> endpoints = new ArrayList<BoundEndpoint>();

        public
        @NotNull
        List<BoundEndpoint> getBoundEndpoints() {
            return endpoints;
        }

        public
        @NotNull
        String getContextPath() {
            // Cannot compute this since we don't know about hostname and port etc
            throw new WebServiceException("Container " + ServletContainer.class.getName() + " doesn't support getContextPath()");
        }
    };

    private final ResourceLoader loader = new ResourceLoader() {
        public URL getResource(String resource) throws MalformedURLException {
            return servletContext.getResource("/WEB-INF/" + resource);
        }
    };

    ServletContainer(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public <T> T getSPI(Class<T> spiType) {
        if (spiType == ServletContext.class) {
            return spiType.cast(servletContext);
        }
        if (spiType.isAssignableFrom(ServletModule.class)) {
            return spiType.cast(module);
        }
        if (spiType == ResourceLoader.class) {
            return spiType.cast(loader);
        }
        return null;
    }
}
