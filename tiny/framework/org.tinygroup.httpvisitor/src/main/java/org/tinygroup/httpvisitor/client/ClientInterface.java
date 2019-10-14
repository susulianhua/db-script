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
import org.tinygroup.httpvisitor.Response;
import org.tinygroup.httpvisitor.manager.HttpTemplateManager;

import java.io.Closeable;
import java.util.List;

/**
 * HTTP客户端接口，具体实现在扩展工程完成
 *
 * @author yancheng11334
 */
public interface ClientInterface extends Closeable {

    public static final String DEFAULT_BEAN_NAME = "httpClientInterface";

    /**
     * 初始化客户端
     *
     * @param context
     */
    void init(Context context);

    /**
     * 执行请求，得到响应
     *
     * @param request
     * @return
     */
    Response execute(Request request);

    /**
     * 获得HTTP通讯配置模板管理器
     *
     * @return
     */
    HttpTemplateManager getHttpTemplateManager();

    /**
     * 允许多例共享通讯资源
     *
     * @return
     */
    boolean allowMultiton();

    /**
     * 获取客户端配置的请求拦截器
     * @return
     */
    List<RequestInterceptor> getInterceptorList();
}
