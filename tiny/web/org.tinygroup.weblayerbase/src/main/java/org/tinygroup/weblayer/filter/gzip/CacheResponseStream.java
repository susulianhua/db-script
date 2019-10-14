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
package org.tinygroup.weblayer.filter.gzip;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class CacheResponseStream extends ServletOutputStream {
    protected boolean closed = false;
    protected HttpServletResponse response = null;
    protected ServletOutputStream output = null;
    protected OutputStream cache = null;

    public CacheResponseStream(HttpServletResponse response,
                               OutputStream cache) throws IOException {
        super();
        closed = false;
        this.response = response;
        this.cache = cache;
    }

    public void close() throws IOException {
        if (closed) {
            throw new IOException(
                    "This output stream has already been closed");
        }
        cache.close();
        closed = true;
    }

    public void flush() throws IOException {
        if (closed) {
            throw new IOException(
                    "Cannot flush a closed output stream");
        }
        cache.flush();
    }

    public void write(int b) throws IOException {
        if (closed) {
            throw new IOException(
                    "Cannot write to a closed output stream");
        }
        cache.write((byte) b);
    }

    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len)
            throws IOException {
        if (closed) {
            throw new IOException(
                    "Cannot write to a closed output stream");
        }
        cache.write(b, off, len);
    }

    public boolean closed() {
        return (this.closed);
    }

    public void reset() {
        //noop
    }
}
