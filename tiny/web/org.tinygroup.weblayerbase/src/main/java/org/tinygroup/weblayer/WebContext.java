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
package org.tinygroup.weblayer;

import org.tinygroup.context.Context;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 功能说明:定义web上下文接口
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-4-28 <br>
 * <br>
 */
public interface WebContext extends Context {
	String REQUEST_ATTRIBUTE_FROM_WEBCONTEXT="REQUEST_ATTRIBUTE_FROM_WEBCONTEXT";
	
    /**
     * 初始化web上下文方法
     *
     * @param request        请求对象
     * @param response       响应对象
     * @param servletContext servlet上下文
     */
    void init(HttpServletRequest request, HttpServletResponse response,
              ServletContext servletContext);

    /**
     * 返回请求上下文关联的http请求对象
     *
     * @return
     */
    HttpServletRequest getRequest();

    /**
     * 设置请求上下文中关联的http请求对象
     *
     * @param request http请求对象
     */
    void setRequest(HttpServletRequest request);

    /**
     * 返回请求上下文中关联的http响应对象
     *
     * @return
     */
    HttpServletResponse getResponse();

    /**
     * 设置请求上下文中关联的http响应对象
     *
     * @param response http响应对象
     */
    void setResponse(HttpServletResponse response);

    /**
     * 获取包装过的WebContext
     *
     * @return
     */
    WebContext getWrappedWebContext();

    /**
     * 取得servletContext对象
     *
     * @return
     */
    ServletContext getServletContext();

    /**
     * 设置servletContext对象
     *
     * @param servletContext
     */
    void setServletContext(ServletContext servletContext);

    /**
     * 把对象设置到scope指定的范围内,相当于调用request、session、servletcontext的setAttribute方法
     *
     * @param scope 代表范围，存在，requestScope、sessionScope、applicationScope
     * @param key   名称
     * @param value 值
     */
    void setObject(String scope, String key, Object value);

    /**
     * 获取指定范围内的对象，相当于调用request、session、servletcontext的getAttribute方法
     *
     * @param scope 代表范围，存在，requestScope、sessionScope、applicationScope
     * @param key   名称
     * @return
     */
    Object getObject(String scope, String key);

    /**
     * 请求是否已终止，例如请求已被重定向了，该请求将被终止，返回true。还有访问的页面已被缓存，可以直接从缓存中获取页面信息，也将返回true，
     * 默认实现是返回false。
     *
     * @return
     */
    boolean isRequestFinished();

}
