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
package org.tinygroup.jspengine.appserv;

import javax.servlet.http.HttpServletRequest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Abstract class allowing a backend appserver instance to retrieve information
 * about the original client request that was intercepted by an SSL
 * terminating proxy server (e.g., load balancer).
 * <p>
 * An implementation of this abstract class inspects a given request for
 * the custom request headers through which the proxy server communicates the
 * information about the original client request to the appserver instance,
 * and makes this information available to the appserver.
 * <p>
 * This allows the appserver to work with any number of 3rd party SSL
 * offloader implementations configured on the front-end web server, for
 * which a corresponding ProxyHandler implementation has been configured
 * on the backend appserver.
 */
public abstract class ProxyHandler {

    /**
     * Gets the SSL client certificate chain with which the client
     * had authenticated itself to the SSL offloader, and which the
     * SSL offloader has added as a custom request header on the
     * given request.
     *
     * @param request The request from which to retrieve the SSL client
     *                certificate chain
     * @return Array of java.security.cert.X509Certificate instances
     * representing the SSL client certificate chain, or null if this
     * information is not available from the given request
     * @throws CertificateException if the certificate chain retrieved
     *                              from the request header cannot be parsed
     */
    public X509Certificate[] getSSLClientCertificateChain(
            HttpServletRequest request)
            throws CertificateException {
        return null;
    }

    /**
     * Returns the SSL keysize with which the original client request that
     * was intercepted by the SSL offloader has been protected, and which
     * the SSL offloader has added as a custom request header on the
     * given request.
     *
     * @param request The request from which to retrieve the SSL key
     *                size
     * @return SSL keysize, or -1 if this information is not available from
     * the given request
     */
    public int getSSLKeysize(HttpServletRequest request) {
        return -1;
    }

    /**
     * Gets the Internet Protocol (IP) address of the original client request
     * that was intercepted by the proxy server.
     *
     * @param request The request from which to retrieve the IP address of the
     *                original client request
     * @return IP address of the original client request, or null if this
     * information is not available from the given request
     */
    public String getRemoteAddress(HttpServletRequest request) {
        return null;
    }


    /**
     * Gets the port of the client request that was intercepted by the
     * proxy server.
     *
     * @param request The request from which to retrieve the port
     *                of the original client request
     * @return the port of the original client request, or -1 if this
     * information is not available from the given request
     */
    public int getRemotePort(HttpServletRequest request) {
        return -1;
    }

}
