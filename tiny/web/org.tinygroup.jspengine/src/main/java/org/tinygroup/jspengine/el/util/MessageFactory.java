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
package org.tinygroup.jspengine.el.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: tcfujii $
 */
public final class MessageFactory {

    protected final static ResourceBundle bundle = ResourceBundle
            .getBundle("com.sun.el.Messages");

    /**
     *
     */
    public MessageFactory() {
        super();
    }

    public static String get(final String key) {
        return bundle.getString(key);
    }

    public static String get(final String key, final Object obj0) {
        return getArray(key, new Object[]{obj0});
    }

    public static String get(final String key, final Object obj0,
                             final Object obj1) {
        return getArray(key, new Object[]{obj0, obj1});
    }

    public static String get(final String key, final Object obj0,
                             final Object obj1, final Object obj2) {
        return getArray(key, new Object[]{obj0, obj1, obj2});
    }

    public static String get(final String key, final Object obj0,
                             final Object obj1, final Object obj2, final Object obj3) {
        return getArray(key, new Object[]{obj0, obj1, obj2, obj3});
    }

    public static String get(final String key, final Object obj0,
                             final Object obj1, final Object obj2, final Object obj3,
                             final Object obj4) {
        return getArray(key, new Object[]{obj0, obj1, obj2, obj3, obj4});
    }

    public static String getArray(final String key, final Object[] objA) {
        return MessageFormat.format(bundle.getString(key), objA);
    }

}
