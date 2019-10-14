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
package org.tinygroup.flow.test.testcase;


public class DataUtil {
    public static final int defaultValue = 0;
    private static int i = defaultValue;

    public static int defaultValue() {
        return defaultValue;
    }

    public static void plus(int j) {
        i = i + j;
    }

    public static void imsub(int j) {
        i = i - j;
    }

    public static int getData() {
        return i;
    }

    public static void reset() {
        i = 0;
    }
}
