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
package org.tinygroup.httpvisitor.client;

import org.tinygroup.context.Context;
import org.tinygroup.httpvisitor.Request;
import org.tinygroup.httpvisitor.RequestInterceptor;
import org.tinygroup.httpvisitor.config.HttpConfigTemplate;
import org.tinygroup.httpvisitor.config.HttpConfigTemplateContext;
import org.tinygroup.httpvisitor.execption.HttpVisitorException;
import org.tinygroup.httpvisitor.manager.HttpTemplateManager;
import org.tinygroup.httpvisitor.struct.Parameter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractClientInterface implements ClientInterface {

    protected static final Charset DEFAULT_REQUEST_CHARSET = Charset
            .forName("ISO-8859-1");

    protected HttpConfigTemplate httpConfigTemplate;

    protected HttpTemplateManager httpTemplateManager;

    public HttpTemplateManager getHttpTemplateManager() {
        return httpTemplateManager;
    }

    public void setHttpTemplateManager(HttpTemplateManager manager) {
        this.httpTemplateManager = manager;
    }

    protected void updateHttpConfigTemplate(Context context) {
        if (context instanceof HttpConfigTemplateContext) {
            HttpConfigTemplateContext httpConfigTemplateContext = (HttpConfigTemplateContext) context;
            httpConfigTemplate = httpConfigTemplateContext.getTemplate();
        }
    }

    public List<RequestInterceptor> getInterceptorList() {
        if (httpConfigTemplate != null) {
            return httpConfigTemplate.getInterceptorList();
        }
        return new ArrayList<RequestInterceptor>();
    }

    /**
     * 得到带参数的URL
     *
     * @param request
     * @return
     */
    protected String getUrl(Request request) {
        StringBuilder sb = new StringBuilder(request.getUrl());
        if (request.getParameters() != null) {
            if (request.getUrl().indexOf("?") < 0) {
                sb.append("?");
            }
            for (Parameter p : request.getParameters()) {
                Object value = p.getValue();
                String key = p.getName();
                try {
                    if (value.getClass().isArray()) {
                        Object[] arrayValue = (Object[]) value;
                        for (Object o : arrayValue) {
                            appendParameter(request, sb, key, o);
                        }
                    } else {
                        appendParameter(request, sb, key, value);
                    }
                } catch (Exception e) {
                    throw new HttpVisitorException("创建URL发生异常", e);
                }

            }
        }
        return sb.toString();
    }

    protected Charset getCharset(Request request) {
        return request.getCharset() == null ? DEFAULT_REQUEST_CHARSET : request.getCharset();
    }

    private void appendParameter(Request request, StringBuilder sb, String key, Object value) throws UnsupportedEncodingException {
        sb.append("&");
        sb.append(URLEncoder.encode(key, getCharset(request).name()));
        sb.append("=");
        sb.append(URLEncoder.encode(value.toString(), getCharset(request).name()));
    }
}
