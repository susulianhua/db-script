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
package org.tinygroup.ini;

/**
 * Created by luoguo on 14-3-30.
 */
public final class Utils {
    private static String[] source = {"\t", "\r", "\n", ";", "=", ":"};
    private static String[] encodeDest = {"\\\\t", "\\\\r", "\\\\n", "\\\\;", "\\\\=", "\\\\:"};
    private static String[] decodeSource = {"[\\\\]t", "[\\\\]r", "[\\\\]n", "[\\\\];", "[\\\\]=", "[\\\\]:"};

    private Utils() {
    }

    public static String encode(String string) {
        String str = string;
        for (int i = 0; i < source.length; i++) {
            str = str.replaceAll(source[i], encodeDest[i]);
        }
        return str;
    }

    public static String decode(String string) {
        String str = string;
        for (int i = 0; i < source.length; i++) {
            str = str.replaceAll(decodeSource[i], source[i]);
        }
        return str;
    }


}
