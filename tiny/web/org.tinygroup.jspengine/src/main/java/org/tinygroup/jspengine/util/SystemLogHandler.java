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
package org.tinygroup.jspengine.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;


/**
 * This helper class may be used to do sophisticated redirection of
 * System.out and System.err.
 *
 * @author Remy Maucherat
 */
public class SystemLogHandler extends PrintStream {


    // ----------------------------------------------------------- Constructors


    /**
     * Thread <-> PrintStream associations.
     */
    protected static final ThreadLocal streams = new ThreadLocal();


    // ----------------------------------------------------- Instance Variables
    /**
     * Thread <-> ByteArrayOutputStream associations.
     */
    protected static final ThreadLocal data = new ThreadLocal();
    /**
     * Wrapped PrintStream.
     */
    protected PrintStream wrapped = null;


    /**
     * Construct the handler to capture the output of the given steam.
     */
    public SystemLogHandler(PrintStream wrapped) {
        super(wrapped);
        this.wrapped = wrapped;
    }


    // --------------------------------------------------------- Public Methods

    /**
     * Start capturing thread's output.
     */
    public static void setThread() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        data.set(baos);
        streams.set(new PrintStream(baos));
    }

    /**
     * Stop capturing thread's output and return captured data as a String.
     */
    public static String unsetThread() {
        ByteArrayOutputStream baos =
                (ByteArrayOutputStream) data.get();
        if (baos == null) {
            return null;
        }
        streams.set(null);
        data.set(null);
        return baos.toString();
    }

    public PrintStream getWrapped() {
        return wrapped;
    }


    // ------------------------------------------------------ Protected Methods

    /**
     * Find PrintStream to which the output must be written to.
     */
    protected PrintStream findStream() {
        PrintStream ps = (PrintStream) streams.get();
        if (ps == null) {
            ps = wrapped;
        }
        return ps;
    }


    // ---------------------------------------------------- PrintStream Methods


    public void flush() {
        findStream().flush();
    }

    public void close() {
        findStream().close();
    }

    public boolean checkError() {
        return findStream().checkError();
    }

    protected void setError() {
        //findStream().setError();
    }

    public void write(int b) {
        findStream().write(b);
    }

    public void write(byte[] b)
            throws IOException {
        findStream().write(b);
    }

    public void write(byte[] buf, int off, int len) {
        findStream().write(buf, off, len);
    }

    public void print(boolean b) {
        findStream().print(b);
    }

    public void print(char c) {
        findStream().print(c);
    }

    public void print(int i) {
        findStream().print(i);
    }

    public void print(long l) {
        findStream().print(l);
    }

    public void print(float f) {
        findStream().print(f);
    }

    public void print(double d) {
        findStream().print(d);
    }

    public void print(char[] s) {
        findStream().print(s);
    }

    public void print(String s) {
        findStream().print(s);
    }

    public void print(Object obj) {
        findStream().print(obj);
    }

    public void println() {
        findStream().println();
    }

    public void println(boolean x) {
        findStream().println(x);
    }

    public void println(char x) {
        findStream().println(x);
    }

    public void println(int x) {
        findStream().println(x);
    }

    public void println(long x) {
        findStream().println(x);
    }

    public void println(float x) {
        findStream().println(x);
    }

    public void println(double x) {
        findStream().println(x);
    }

    public void println(char[] x) {
        findStream().println(x);
    }

    public void println(String x) {
        findStream().println(x);
    }

    public void println(Object x) {
        findStream().println(x);
    }

}
