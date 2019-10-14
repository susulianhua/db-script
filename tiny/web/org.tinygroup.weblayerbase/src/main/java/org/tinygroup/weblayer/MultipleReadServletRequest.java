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

import org.tinygroup.commons.io.ByteArray;
import org.tinygroup.commons.io.ByteArrayInputStream;
import org.tinygroup.commons.io.StreamUtil;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * 可以进行多次读取流的请求对象
 *
 * @author renhui
 */
public class MultipleReadServletRequest extends HttpServletRequestWrapper {

    private boolean acquired;
    private BufferedServletInputStream servletInputStream;

    public MultipleReadServletRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (!acquired) {
            ByteArray byteArray = StreamUtil.readBytes(super.getInputStream(),
                    true);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    byteArray.toByteArray());
            servletInputStream = new BufferedServletInputStream(
                    byteArrayInputStream);
            acquired = true;
        }
        servletInputStream.reset();
        return servletInputStream;
    }

    private static class BufferedServletInputStream extends ServletInputStream {
        private ByteArrayInputStream byteArrayInputStream;

        public BufferedServletInputStream(
                ByteArrayInputStream byteArrayInputStream) {
            super();
            this.byteArrayInputStream = byteArrayInputStream;
        }

        @Override
        public synchronized void reset() throws IOException {
            byteArrayInputStream.reset();
        }

        @Override
        public int read() throws IOException {
            return byteArrayInputStream.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return byteArrayInputStream.read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return byteArrayInputStream.read(b, off, len);
        }

        @Override
        public long skip(long n) throws IOException {
            return byteArrayInputStream.skip(n);
        }

        @Override
        public int available() throws IOException {
            return byteArrayInputStream.available();
        }

        @Override
        public void close() throws IOException {
            byteArrayInputStream.close();
        }

        @Override
        public synchronized void mark(int readlimit) {
            byteArrayInputStream.mark(readlimit);
        }

        @Override
        public boolean markSupported() {
            return byteArrayInputStream.markSupported();
        }

    }

}
