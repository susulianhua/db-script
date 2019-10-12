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
package com.xquant.xml;

import com.thoughtworks.xstream.XStream;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luoguo
 */
public final class XStreamFactory {

    private static Map<String, XStream> xstreamMap = new HashMap<String, XStream>();

    private XStreamFactory() {
    }

    public static XStream getXStream() {
        return getXStream("");
    }

    public static XStream getXStream(String key) {
        String xstreamKey = key;
        if (key == null) {
            xstreamKey = "";
        }
        XStream xstream = xstreamMap.get(xstreamKey);
        if (xstream == null) {
            xstream = newXStream(null);
            xstreamMap.put(xstreamKey, xstream);
        }
        return xstream;
    }

    public static XStream getXStream(String key, ClassLoader classLoader) {
        String xstreamKey = key;
        if (key == null) {
            xstreamKey = "";
        }
        XStream xstream = xstreamMap.get(xstreamKey);
        if (xstream == null) {
            xstream = newXStream(classLoader);
            xstreamMap.put(xstreamKey, xstream);
        }
        return xstream;
    }

    private static XStream newXStream(ClassLoader classLoader) {
        XStream xstream = new XStream();
        if (classLoader != null) {
            xstream.setClassLoader(classLoader);
        } else {
            xstream.setClassLoader(XStreamFactory.class.getClassLoader());
        }
        xstream.autodetectAnnotations(true);
        xstream.setMode(XStream.NO_REFERENCES);
        return xstream;
    }

    public static void clear() {
        xstreamMap.clear();
    }
}
