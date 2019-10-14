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
package org.tinygroup.httpvisitor;

import org.tinygroup.httpvisitor.struct.Parameter;

import java.nio.charset.Charset;
import java.util.List;


/**
 * HTTP请求对象(框架构建，无需考虑包装)
 *
 * @author yancheng11334
 */
public class Request {

    private final MethodMode method;
    private final String url;
    private final List<Header> headers;
    private final List<Cookie> cookies;
    private final List<Parameter> parameters;
    private final List<BodyElement> bodyElements;
    private final Charset charset;

    public Request(MethodMode method, String url, List<Header> headers,
                   List<Cookie> cookies, List<Parameter> parameters,
                   List<BodyElement> bodyElements, Charset charset) {
        super();
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.cookies = cookies;
        this.parameters = parameters;
        this.bodyElements = bodyElements;
        this.charset = charset;
    }

    public List<BodyElement> getBodyElements() {
        return bodyElements;
    }

    public MethodMode getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public Header getHeader(String name) {
        if (headers != null) {
            for (Header header : headers) {
                if (header.getName().equals(name)) {
                    return header;
                }
            }
        }
        return null;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public Parameter getParameter(String name) {
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                if (parameter.getName().equals(name)) {
                    return parameter;
                }
            }
        }
        return null;
    }

    public Charset getCharset() {
        return charset;
    }

}
