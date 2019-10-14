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
package org.tinygroup.weblayer.filter;

import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.basic.impl.BasicWebContextImpl;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 对输入、输出的数据进行安全检查，排除可能的攻击。例如：XSS过滤、CRLF换行回车过滤等。
 */
public class BasicTinyFilter extends AbstractTinyFilter {

    private static final String MAX_COOKIE_SIZE = "maxCookieSize";
    private Object[] interceptors;// 拦截器列表
    private String maxCookieSize;// 最大Cookie的大小

    public void setInterceptors(Object[] interceptors) {
        this.interceptors = interceptors;
    }

    public void setMaxCookieSize(String maxCookieSize) {
        this.maxCookieSize = maxCookieSize;
    }

    protected void customInit() {
        if (maxCookieSize == null) {
            maxCookieSize = get(MAX_COOKIE_SIZE);
        }
    }


    public void preProcess(WebContext context) throws ServletException, IOException {
        BasicWebContextImpl basic = (BasicWebContextImpl) context;
        basic.prepareResponse();
    }


    public void postProcess(WebContext context) throws ServletException, IOException {
        BasicWebContextImpl basic = (BasicWebContextImpl) context;
        basic.commitResponse();
    }


    public WebContext getAlreadyWrappedContext(WebContext wrappedContext) {
        BasicWebContextImpl basic = new BasicWebContextImpl(wrappedContext);
        basic.setMaxCookieSize(maxCookieSize);
        return basic;
    }


    protected void initContext(WebContext context) {
        super.initContext(context);
        ((BasicWebContextImpl) context).initContext(interceptors);
    }


    public int getOrder() {
        return BASIC_FILTER_PRECEDENCE;
    }


}
