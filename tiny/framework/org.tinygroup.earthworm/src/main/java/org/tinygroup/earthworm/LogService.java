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

import org.tinygroup.earthworm.impl.BaseLogSupport;
import org.tinygroup.earthworm.log.LogUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class LogService {
    private static final String FIX_SIGN = " ";
    private final static Logger LOGGER = LoggerFactory.getLogger(LogService.class);
    private final static LogService logService;
    private final static String STAGE_KEY = "earthworm_stage";
    private final static String SPAN_KEY = "earthworm_span";
    private final static String TRACE_KEY = "earthworm_trace";
    private final static String LOCAL_SPAN_KEY = "earthworm_local_span";
    private final static String LOCAL_SPAN_ID = "earthworm_local_id";
    private static LogSupport logSupport = new BaseLogSupport();

    static {
        logService = new LogService();

    }

    protected static LogSupport getLogSupport() {
//		if(logSupport==null){
//			LOGGER.errorMessage("use BaseLogSupport");
//			logSupport = new BaseLogSupport();
//		}
        return logSupport;
    }

    public static void setLogSupport(String logSupportClass) {
        try {
            logSupport = (LogSupport) Class.forName(logSupportClass).newInstance();
            LOGGER.infoMessage("using logSupportClass class:{}", logSupportClass);
        } catch (Exception e) {
            LOGGER.warnMessage("set logSupportClass exception,class:{},reason:{}",
                    logSupportClass, e.getClass().getName());
        }

    }

    public static LogService getInstance() {
        return logService;
    }

    public void clear() {
        LoggerFactory.removeThreadVariable(STAGE_KEY);
        LoggerFactory.removeThreadVariable(SPAN_KEY);
        LoggerFactory.removeThreadVariable(TRACE_KEY);
        LoggerFactory.removeThreadVariable(LOCAL_SPAN_KEY);
        LoggerFactory.removeThreadVariable(LOCAL_SPAN_ID);
    }

    public void changeMDC() {
        RpcContext rc = RpcContext.get();
        if (rc != null) {
            LoggerFactory.putThreadVariable(SPAN_KEY, fixBlankToString(rc.spanId, 10));
            LoggerFactory.putThreadVariable(TRACE_KEY, fixBlankToString(rc.traceId, 10));
            if (rc.local != null && !rc.local.isEnd()) {
                LoggerFactory.putThreadVariable(LOCAL_SPAN_KEY, fixBlankToString(rc.local.spanId, 10));
                LoggerFactory.putThreadVariable(LOCAL_SPAN_ID, fixBlankToString(rc.local.localId, 10));
            } else {
                LoggerFactory.putThreadVariable(LOCAL_SPAN_KEY, fixBlankToString("", 10));
                LoggerFactory.putThreadVariable(LOCAL_SPAN_ID, fixBlankToString("", 10));
            }
        } else {
            clear();
        }

    }

    public String fixBlankToString(String str, int length) {
        if (length < str.length()) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < length - str.length(); i++) {
            sb.append(FIX_SIGN);
        }
        return sb.toString();
    }

    public void logStage(String stage) {
        LogUtil.logInfo(getLogSupport().getLogMessage(stage));
        //LOGGER.infoMessage("{}",getLogSupport().getLogMessage(stage));
    }

    public void logStage(String stage, long currentTimeMillis) {
        LogUtil.logInfo(getLogSupport().getLogMessage(stage, currentTimeMillis));
    }
}
