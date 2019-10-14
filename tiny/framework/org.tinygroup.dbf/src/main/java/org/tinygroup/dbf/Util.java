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
package org.tinygroup.dbf;

/**
 * Created by luoguo on 2014/4/26.
 */
public final class Util {

    public static final int MAX_MINI_UINT = 256;

    private Util() {
    }

    public static int getUnsignedInt(byte[] bytes, int start, int length) {
        int value = 0;
        for (int i = 0; i < length; i++) {
            value += getIntValue(bytes[start + i], i);
        }
        return value;
    }

    public static int getIntValue(byte b, int bytePos) {
        int v = 1;
        for (int i = 0; i < bytePos; i++) {
            v = v << 8;
        }
        return getUnsignedInt(b) * v;

    }

    public static int getUnsignedInt(byte byteValue) {
        if (byteValue < 0) {
            return byteValue + MAX_MINI_UINT;
        } else {
            return byteValue;
        }
    }

    ///added by wcg;
    public static byte[] getByteFromInt(int value, int bit) {
        byte[] result = new byte[bit];
        for (int i = 0; i < bit; i++) {
            int pos = i << 3;
            result[i] = (byte) (((0xff << pos) & value) >> pos);
        }
        return result;
    }


    public static Boolean getBooleanValue(String stringValue) {
        String value = stringValue.toLowerCase();
        if (("t").equals(value) || ("y").equals(value)) {
            return true;
        }
        if (("f").equals(value) || ("n").equals(value)) {
            return false;
        }
        return null;
    }
}
