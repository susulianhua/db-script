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
package org.tinygroup.httpvisitor.request;

import org.tinygroup.httpvisitor.Cookie;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * HTTP链式构造器接口
 *
 * @param <T>
 * @author yancheng11334
 */
public interface HttpRequestBuilderInterface<T> {

    /**
     * 设置请求编码
     *
     * @param charset
     * @return
     */
    T charset(String charset);

    /**
     * 设置请求编码
     *
     * @param charset
     * @return
     */
    T charset(Charset charset);

    /**
     * 增加单条URL参数
     *
     * @param name
     * @param value
     * @return
     */
    T param(String name, Object value);

    /**
     * 批量增加URL参数
     *
     * @param maps
     * @return
     */
    T params(Map<String, Object> maps);

    /**
     * 增加单条HTTP报文首部参数
     *
     * @param name
     * @param value
     * @return
     */
    T header(String name, String value);

    /**
     * 批量增加HTTP报文首部参数
     *
     * @param maps
     * @return
     */
    T headers(Map<String, String> maps);

    /**
     * 增加单个cookie
     *
     * @param domain
     * @param name
     * @param value
     * @return
     */
    T cookie(String domain, String name, String value);

    /**
     * 增加单个cookie
     *
     * @param cookie
     * @return
     */
    T cookie(Cookie cookie);

    /**
     * 批量增加cookie
     *
     * @param cookies
     * @return
     */
    T cookies(Map<String, Cookie> cookies);


}
