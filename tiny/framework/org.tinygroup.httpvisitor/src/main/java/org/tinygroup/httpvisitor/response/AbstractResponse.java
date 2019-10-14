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
package org.tinygroup.httpvisitor.response;

import org.tinygroup.commons.io.ByteArrayOutputStream;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.httpvisitor.Cookie;
import org.tinygroup.httpvisitor.Header;
import org.tinygroup.httpvisitor.Response;
import org.tinygroup.httpvisitor.execption.HttpVisitorException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

/**
 * 抽象的HTTP响应基类
 *
 * @author yancheng11334
 */
public abstract class AbstractResponse implements Response {

    protected Charset charset;

    public AbstractResponse() {
        this.charset = Charset.forName("UTF-8");
    }

    public Response charset(String charset) {
        this.charset = Charset.forName(charset);
        return self();
    }

    public Response charset(Charset charset) {
        this.charset = charset;
        return self();
    }

    public Header getHeader(String name) {
        Header[] headers = getHeaders();
        if (headers != null) {
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].getName().equals(name)) {
                    return headers[i];
                }
            }
        }
        return null;
    }

    public Cookie getCookie(String name) {
        Cookie[] cookies = getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(name)) {
                    return cookies[i];
                }
            }
        }
        return null;
    }

    public String text() {
        byte[] b = bytes();
        return b == null ? null : new String(b, charset);
    }

    public byte[] bytes() {
        try {
            InputStream stream = stream();
            if (stream == null) {
                return null;
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            StreamUtil.io(stream, outputStream, false, false);
            return outputStream.toByteArray().toByteArray();
        } catch (IOException e) {
            throw new HttpVisitorException("读取数组发生异常", e);
        }
    }


    public InputStream stream() {
        try {
            return isGzip() ? getGzipInputStream() : getSourceInputStream();
        } catch (IOException e) {
            throw new HttpVisitorException("读取流发生异常", e);
        }
    }

    /**
     * 判断流是否采用GZIP压缩
     *
     * @return
     */
    protected boolean isGzip() {
        Header responseHeader = this.getHeader("Content-Encoding");
        if (responseHeader != null && "gzip".equals(responseHeader.getValue())) {
            return true;
        }
        return false;
    }

    /**
     * 通过Gzip包装的流
     *
     * @return
     * @throws IOException
     */
    protected InputStream getGzipInputStream() throws IOException {
        return new GZIPInputStream(getSourceInputStream());
    }

    /**
     * 得到原始流
     *
     * @return
     * @throws IOException
     */
    protected abstract InputStream getSourceInputStream() throws IOException;

    protected abstract Response self();
}
