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
package org.tinygroup.earthworm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WormBaseContext {

    static final int version = 1;
    static final String KV_SPLITER = ":";
    static final String KV_ITEM_SPLITER = ";";
    static final int DEFAULT_LOCAL_INDEX = 1;
    static final int DEFAULT_RPC_INDEX = 1;
    String relationId = "";
    String traceId = "";
    String spanId = "";
    String traceName = "";
    long startTime;
    int milestone;
    int endTime;
    String tag = "";
    AtomicInteger localIdx;
    String localId = "";
    String errorMsg = "";
    String errorCode = "";
    Map<String, String> customInofos = new HashMap<String, String>();
    Map<String, String> localAttributes = new HashMap<String, String>();

    WormBaseContext(String _traceId, String _spanId) {
        this(_traceId, _spanId, new AtomicInteger(0));
    }

    WormBaseContext(String _traceId, String _spanId, AtomicInteger _localIdx) {
        this.localId = "";
        this.localIdx = _localIdx;
        this.traceName = "";
        this.tag = "";
        this.traceId = _traceId;
        this.spanId = _spanId;
        this.errorMsg = "";
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Map<String, String> map = totalMap();
        for (String key : map.keySet()) {
            if (sb.length() != 0) {
                sb.append(KV_ITEM_SPLITER);
            }
            sb.append(key).append(KV_SPLITER).append(map.get(key));
        }
        return sb.toString();
    }

    public Map<String, String> totalMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("trace_id", traceId);
        map.put("span_id", spanId);
        map.put("relationId", relationId);
        map.put("traceName", traceName);
        map.put("tag", tag);
        map.put("name", tag);
        map.put("startTime", startTime + "");
        map.put("milestone", milestone + "");
        map.put("endTime", endTime + "");
        map.put("errorMsg", errorMsg);
        map.put("localIdx", localIdx + "");
        map.put("localId", localId);
        return map;
    }
}
