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

import org.tinygroup.commons.order.Ordered;
import org.tinygroup.weblayer.webcontextfactory.WebContextFactory;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * WebContext过滤器，用于根据WebContext进行相关处理
 *
 * @author luoguo
 */
public interface TinyFilter extends Ordered, WebContextFactory<WebContext> {

    String TINY_FILTER = "tiny-filter";
    String TINY_WRAPPER_FILTER = "tiny-wrapper-filter";

    int BASIC_FILTER_PRECEDENCE = HIGHEST_PRECEDENCE;//BasicTinyFilter优先级最高
    int PARSER_FILTER_PRECEDENCE = HIGHEST_PRECEDENCE + 100;
    int BUFFERED_FILTER_PRECEDENCE = HIGHEST_PRECEDENCE + 200;
    int LAZY_COMMIT_FILTER_PRECEDENCE = HIGHEST_PRECEDENCE + 300;
    int SESSION_FILTER_PRECEDENCE = HIGHEST_PRECEDENCE + 400;
    int SETLOCALE_FILTER_PRECEDENCE = HIGHEST_PRECEDENCE + 500;
    int REWRITE_FILTER_PRECEDENCE = HIGHEST_PRECEDENCE + 600;


    /**
     * tiny过滤器前置处理方法
     *
     * @param context 请求上下文
     * @throws ServletException
     * @throws IOException
     */
    void preProcess(WebContext context) throws ServletException, IOException;

    /**
     * tiny过滤器后置操作方法
     *
     * @param context 请求上下文
     * @throws ServletException
     * @throws IOException
     */
    void postProcess(WebContext context) throws ServletException, IOException;

    /**
     * tiny过滤器的初始化方法
     *
     * @param config tiny过滤器配置对象
     * @throws ServletException
     */
    void initTinyFilter(TinyFilterConfig config) throws ServletException;

    /**
     * tiny过滤器的销毁方法
     */
    void destroyTinyFilter();

    /**
     * tiny过滤器的匹配方法,此过滤器是否可以处理此请求路径
     *
     * @param url 请求路径
     * @return
     */
    boolean isMatch(String url);

    /**
     * 返回tiny过滤器名称
     *
     * @return
     */
    String getFilterName();

    /**
     * 设置tiny过滤器名称
     *
     * @param filterName 过滤器名称
     */
    void setFilterName(String filterName);

}
