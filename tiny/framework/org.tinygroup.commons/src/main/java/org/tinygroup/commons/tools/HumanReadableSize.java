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
package org.tinygroup.commons.tools;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.tinygroup.commons.tools.Assert.assertNotNull;
import static org.tinygroup.commons.tools.Assert.assertTrue;
import static org.tinygroup.commons.tools.CollectionUtil.createHashMap;
import static org.tinygroup.commons.tools.StringUtil.trimToNull;

/**
 * 以易读的方式来解析和显示字节数。
 *
 * @author renhui
 */
public class HumanReadableSize {
    private final static Long ONE = 1L;
    private final static Long ONE_KILO = 1024L;
    private final static Long ONE_MEGA = 1024 * 1024L;
    private final static Long ONE_GIGA = 1024 * 1024 * 1024L;
    private final static Long ONE_TERA = 1024 * 1024 * 1024 * 1024L;
    private final static Map<String, Long> UNIT_NAMES = createHashMap();
    private final static Pattern REGEXP = Pattern.compile("(\\d+(\\.\\d+)?)\\s*(K|M|G|T)?", Pattern.CASE_INSENSITIVE);

    static {
        UNIT_NAMES.put("B", ONE);
        UNIT_NAMES.put("K", ONE_KILO);
        UNIT_NAMES.put("M", ONE_MEGA);
        UNIT_NAMES.put("G", ONE_GIGA);
        UNIT_NAMES.put("T", ONE_TERA);
    }

    private final long value;
    private final String humanReadable;

    public HumanReadableSize(String humanReadable) {
        this.value = parse(humanReadable, "-1", "n/a");
        this.humanReadable = toHumanReadble(this.value);
    }

    public HumanReadableSize(long value) {
        this.value = value < 0 ? -1 : value;
        this.humanReadable = toHumanReadble(this.value);
    }

    /**
     * 将易读的字节数转换成真实字节数。
     * <ul>
     * <li><code>1</code> 转换成 <code>1</code>。</li>
     * <li><code>1K</code> 转换成 <code>1024</code>。</li>
     * <li><code>1M</code> 转换成 <code>1048576</code>。</li>
     * <li><code>1G</code> 转换成 <code>1073741824</code>。</li>
     * <li><code>1T</code> 转换成 <code>1099511627776</code>。</li>
     * <li>支持小数点，例如：<code>1.5K</code>。</li>
     * </ul>
     */
    public static long parse(String humanReadableSize) {
        return parse(humanReadableSize, (String[]) null);
    }

    private static long parse(String humanReadableSize, String... nas) {
        humanReadableSize = assertNotNull(trimToNull(humanReadableSize), "human readble size");

        if (nas != null) {
            for (String na : nas) {
                if (na != null && na.equalsIgnoreCase(humanReadableSize)) {
                    return -1;
                }
            }
        }

        Matcher matcher = REGEXP.matcher(humanReadableSize);

        assertTrue(matcher.matches(), "wrong format: %s", humanReadableSize);

        double size = Double.parseDouble(matcher.group(1));
        String unit = trimToNull(matcher.group(3));

        if (unit != null) {
            size *= UNIT_NAMES.get(unit.toUpperCase());
        }

        return (long) size;
    }

    /**
     * 将字节数取整，并转换成易读的概要字节数。
     * <ul>
     * <li><code>1</code>转换成<code>1</code>。</li>
     * <li><code>1024</code>转换成<code>1K</code>。</li>
     * <li><code>1048576</code>转换成<code>1M</code>。</li>
     * <li><code>1073741824</code>转换成<code>1G</code>。</li>
     * <li><code>1099511627776</code>转换成<code>1T</code>。</li>
     * <li>小于<code>0</code>的一律转换成<code>n/a</code>。</li>
     * </ul>
     */
    public static String toHumanReadble(long size) {
        if (size < 0) {
            return "n/a";
        }

        DecimalFormat format = new DecimalFormat("#.##");

        if (size / ONE_TERA > 0) {
            return format.format((double) size / ONE_TERA) + "T";
        }

        if (size / ONE_GIGA > 0) {
            return format.format((double) size / ONE_GIGA) + "G";
        }

        if (size / ONE_MEGA > 0) {
            return format.format((double) size / ONE_MEGA) + "M";
        }

        if (size / ONE_KILO > 0) {
            return format.format((double) size / ONE_KILO) + "K";
        }

        return String.valueOf(size);
    }

    public long getValue() {
        return value;
    }

    public String getHumanReadable() {
        return humanReadable;
    }


    public int hashCode() {
        return 31 + (int) (value ^ value >>> 32);
    }


    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (obj instanceof HumanReadableSize) {
            return value == ((HumanReadableSize) obj).value;
        }

        return false;
    }


    public String toString() {
        return humanReadable;
    }
}
