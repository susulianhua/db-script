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

import java.io.Closeable;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * HTTP响应结果(统一不同底层实现)
 *
 * @author yancheng11334
 */
public interface Response extends Closeable {

    StatusLine getStatusLine();

    Header[] getHeaders();

    Header getHeader(String name);

    Cookie[] getCookies();

    Cookie getCookie(String name);

    /**
     * 设置响应编码
     *
     * @param charset
     * @return
     */
    Response charset(String charset);

    /**
     * 设置响应编码
     *
     * @param charset
     * @return
     */
    Response charset(Charset charset);

    /**
     * 得到文本内容
     *
     * @return
     */
    String text();

    /**
     * 得到字节数组
     *
     * @return
     */
    byte[] bytes();

    /**
     * 得到流
     *
     * @return
     */
    InputStream stream();

}
