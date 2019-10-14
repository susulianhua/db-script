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

import javax.servlet.http.HttpServletRequest;

public abstract class CorsUtils {

    /** The CORS {@code Access-Control-Request-Method} request header field name.
     * @see <a href="http://www.w3.org/TR/cors/">CORS W3C recommandation</a>
     */
    public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    /**
     * The HTTP {@code Origin} header field name.
     * @see <a href="http://tools.ietf.org/html/rfc6454">RFC 6454</a>
     */
    private static final String ORIGIN = "Origin";

    /**
     * Returns {@code true} if the request is a valid CORS one.
     */
    public static boolean isCorsRequest(HttpServletRequest request) {
        return (request.getHeader(ORIGIN) != null);
    }

    /**
     * Returns {@code true} if the request is a valid CORS pre-flight one.
     */
    public static boolean isPreFlightRequest(HttpServletRequest request) {
        return (isCorsRequest(request) && HttpMethod.OPTIONS.matches(request.getMethod()) &&
                request.getHeader(ACCESS_CONTROL_REQUEST_METHOD) != null);
    }

}
