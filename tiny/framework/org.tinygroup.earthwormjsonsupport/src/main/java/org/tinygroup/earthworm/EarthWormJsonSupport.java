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

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.util.TinyConfig;
import org.tinygroup.config.util.TinyConfigConstants;
import org.tinygroup.earthworm.util.EarthWormHelper;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class EarthWormJsonSupport implements LogSupport {
    private final static String STATUS_SUCCESS = "0";
    private final static String STATUS_FAIL = "1";
    private final static String R0 = "r0";
    private final static String A0 = "a0";
    private final static String BDQX_PRE = "";
    private final static String RESULT_OF_SKIP = "";
    private final static String HEART_BEAT = "heart_beat_key_node_client";
    private final static String KEY_INFO = "info";
    private final static String KEY_ERRORMSG = "error_info";
    private final static String KEY_ERRORNO = "error_no";
    private final static String KEY_MYPACKAGE = "myPackage";
    private final static Logger LOGGER = LoggerFactory.getLogger(EarthWormJsonSupport.class);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    public String getLogMessage(String stage) {
        return getLogMessage(stage, System.currentTimeMillis());

    }

    @Override
    public String getLogMessage(String stage, long time) {
        RpcContext rpcContext = RpcContext.get();
        // 过滤心跳
        if (HEART_BEAT.equals(rpcContext.tag)) {
            return RESULT_OF_SKIP;
        }
        // 过滤其他阶段
        String depotID = "";
        if (EarthWormHelper.STAGE_SS.equals(stage) || EarthWormHelper.STAGE_CR.equals(stage)) {
            depotID = A0;
        } else if (EarthWormHelper.STAGE_CS.equals(stage) || EarthWormHelper.STAGE_SR.equals(stage)) {
            depotID = R0;
        }
        if (StringUtil.isBlank(depotID)) {
            return RESULT_OF_SKIP;
        }
        JSONObject json = new JSONObject();
        // 0成功 1失败
        String responseStatus = STATUS_FAIL;
        // 版本固定
        String version = STATUS_SUCCESS;
        // 固定
        String spanType = STATUS_SUCCESS;
        // 服务id
        String serviceName = rpcContext.tag;
        if (StringUtil.isBlank(rpcContext.errorMsg)) {
            responseStatus = STATUS_SUCCESS;
        } else {
            rpcContext.customInofos.put(KEY_ERRORMSG, rpcContext.errorMsg);
            rpcContext.customInofos.put(KEY_ERRORNO, rpcContext.errorCode);
        }
        // 适配bdqx的

        String spanId = BDQX_PRE + rpcContext.spanId;

        //trace_id区分调用链消息的唯一标识，只能是字母和数字的组合必填
        //span_id跨度层级必填
        //depot_id跨度里的层级必填
        //module_type （只有CS、SR、SS、CR四种）必填
        //timestamp时间戳(yyyy-MM-dd HH:mm:ss.SSS)必填
        //name服务名/节点名必填
        //ipIP地址 必填
        //responseStatus0表示成功，1表示失败必填
        //versionV2.3.1选填，没有填””
        //spanType0：消息插件处理，1：proc处理，2：数据库访问，3：缓存访问选填，没有填””
        //serverName服务名称选填，没有填””
        String host = TinyConfig.getValue(TinyConfigConstants.TINY_NODE_HOST);
        if (StringUtil.isBlank(host)) {
            host = rpcContext.target;
        }
        String[] info = new String[]{rpcContext.traceId, spanId, depotID, stage, dateFormat.format(new Date(time)),
                rpcContext.target, host, responseStatus, version, spanType, serviceName};
        json.put(KEY_INFO, info);
        JSONObject myPackage = new JSONObject();
        Map<String, String> customInofos = rpcContext.customInofos;

        if (MapUtils.isEmpty(customInofos)) {
            String jsonString = json.toJSONString();
            LOGGER.debugMessage("{}", jsonString);
            return jsonString;
        }
        for (String key : customInofos.keySet()) {
            myPackage.put(key, customInofos.get(key));
        }
        json.put(KEY_MYPACKAGE, myPackage.toJSONString());
        String jsonString = json.toJSONString();
        LOGGER.debugMessage("{}", jsonString);
        return jsonString;
    }

}
