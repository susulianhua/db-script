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
package org.tinygroup.earthworm.util;

import org.tinygroup.commons.tools.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EarthWormHelper {
    // ========TYPE========
    public static final int SPANTYPE_MESSAGE = 0;
    public static final int SPANTYPE_PROC = 1;
    public static final int SPANTYPE_DATABASE = 2;
    public static final int SPANTYPE_CACHE = 3;
    // ========STAGE========
    public static final String STAGE_START_RPC = "startRpc";
    public static final String STAGE_CS = "CS";
    public static final String STAGE_CR = "CR";
    public static final String STAGE_SR = "SR";
    public static final String STAGE_SS = "SS";
    public static final String STAGE_START_TRACE = "startTrace";
    public static final String STAGE_END_TRACE = "endTrace";
    public static final String STAGE_START_LOCAL = "startLocal";
    public static final String STAGE_END_LOCAL = "endLocal";
    // ========STAGE========
    public static final String PROPERTY_TARGET = "rpc_property_target";
    public static final String PROPERTY_RELATION = "rpc_property_relation";

    public static String trim(String traceId) {
        if (traceId == null) {
            return traceId;
        }
        return traceId.trim();
    }

    public static boolean validTraceId(String traceId) {
        if (StringUtil.isBlank(traceId)) {
            return false;
        }
        return true;
    }

    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static boolean validSpanId(String spanId) {
        if (StringUtil.isBlank(spanId)) {
            return false;
        }
        return true;
    }

    public static String toString(Map<String, String> map, String itemSpliter, String kvSpliter) {
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            if (sb.length() != 0) {
                sb.append(itemSpliter);
            }
            sb.append(key).append(kvSpliter).append(map.get(key));
        }
        return sb.toString();
    }

    public static Map<String, String> parse(String str, String itemSpliter, String kvSpliter) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtil.isBlank(str)) {
            return map;
        }
        String[] strArray = str.split(itemSpliter);
        for (String strItem : strArray) {
            if (StringUtil.isBlank(strItem)) {
                continue;
            }
            String[] kvArray = strItem.split(kvSpliter);
            if (kvArray.length < 2) {
                continue;
            }
            map.put(kvArray[0], kvArray[1]);
        }
        return map;
    }
}
