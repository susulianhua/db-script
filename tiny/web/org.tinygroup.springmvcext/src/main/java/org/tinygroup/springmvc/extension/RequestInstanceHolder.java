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
package org.tinygroup.springmvc.extension;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 在线程上下文中保持ExtensionMappingInstance实例和ServletWebRequest
 *
 * @author renhui
 */
public class RequestInstanceHolder {

    private static final ThreadLocal<ExtensionMappingInstance> extensionLocal = new ThreadLocal<ExtensionMappingInstance>();

    /**
     * request local
     */
    private static final ThreadLocal<ServletWebRequest> reqLocal = new ThreadLocal<ServletWebRequest>();

    private static final ThreadLocal<String> extension = new ThreadLocal<String>();

    public static ExtensionMappingInstance getMappingInstance() {
        return extensionLocal.get();
    }

    public static void setMappingInstance(
            ExtensionMappingInstance mappingInstance) {
        extensionLocal.set(mappingInstance);
    }

    /**
     * 从本地线程中取得{@link ServletWebRequest}对象。
     *
     * @return
     */
    public static ServletWebRequest getServletWebRequest() {
        return reqLocal.get();
    }

    /**
     * 在本地线程中设置{@link ServletWebRequest}对象。
     *
     * @param servletWebRequest
     */
    public static void setServletWebRequest(ServletWebRequest servletWebRequest) {
        reqLocal.set(servletWebRequest);
    }

    public static String getExtension() {
        return extension.get();
    }

    public static void setExtension(String ext) {
        extension.set(ext);
    }

    public static void clearThreadLocal() {
        extensionLocal.set(null);
        reqLocal.set(null);
        extension.set(null);
    }

}
