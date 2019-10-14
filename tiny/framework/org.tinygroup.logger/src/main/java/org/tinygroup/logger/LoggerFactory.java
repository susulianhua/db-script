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
package org.tinygroup.logger;

import org.slf4j.ILoggerFactory;
import org.slf4j.MDC;
import org.tinygroup.logger.impl.LoggerImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 日志工厂，用于获取Logger实例。
 *
 * @author luoguo
 */
public final class LoggerFactory {
    //	public final static String UUID_KEY = "logger.uuid";
    public final static String SERVICE_SERVICEID = "service.serviceId";
    public final static String SERVICE_EVENTID = "service.eventId";
    private static ThreadLocal<Map<String, String>> threadVariableMap = new ThreadLocal<Map<String, String>>() {
    };
    private static Map<String, String> globalMdcMap = new HashMap<String, String>();

    //	private static InheritableThreadLocal<Map<String, String>> threadVariableMap = new InheritableThreadLocal<Map<String, String>>() {
//		protected Map<String, String> childValue(Map<String, String> parentValue) {
//			if (parentValue == null) {
//				return null;
//			}
//			return new HashMap<String, String>(parentValue);
//		}
//
//		@Override
//		protected Map<String, String> initialValue() {
//			HashMap<String, String> hashMap = new HashMap<String, String>();
//			return hashMap;
//		}
//	};
    private static ThreadLocal<Map<String, String>> threadRpcVariableMap = new ThreadLocal<Map<String, String>>() {
    };

//	private static InheritableThreadLocal<Map<String, String>> threadRpcVariableMap = new InheritableThreadLocal<Map<String, String>>() {
//		protected Map<String, String> childValue(Map<String, String> parentValue) {
//			if (parentValue == null) {
//				return null;
//			}
//			return new HashMap<String, String>(parentValue);
//		}
//	};

    private static ThreadLocal<LogLevel> threadLogLevel = new ThreadLocal<LogLevel>();

    private static ConcurrentMap<String, Logger> loggers = new ConcurrentHashMap<String, Logger>();
    private static List<String> rpcVariableKeyList = new ArrayList<String>();

    static {
        rpcVariableKeyList.add(SERVICE_EVENTID);
        rpcVariableKeyList.add(SERVICE_SERVICEID);
    }

    private LoggerFactory() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    public static void putGlobalMdc(String key, String value) {
        globalMdcMap.put(key, value);
    }

    public static Map<String, String> getGlobalMdcMap() {
        return globalMdcMap;
    }

    public static Logger getLogger(String name) {
        if (loggers.containsKey(name)) {
            return loggers.get(name);
        }
        Logger logger = new LoggerImpl(org.slf4j.LoggerFactory.getLogger(name));
        Logger oldLogger = loggers.putIfAbsent(name, logger);
        if (null != oldLogger) {
            logger = oldLogger;
        }
        return logger;
    }

    public static void putThreadVariable(String key, String value) {
        putThreadVariable(key, value, threadVariableMap);
        if (isRpcVariableKey(key)) {
            putThreadVariable(key, value, threadRpcVariableMap);
        }
    }

    private static void putThreadVariable(String key, String value,
                                          ThreadLocal<Map<String, String>> map) {
        Map<String, String> valueMap = map.get();
        if (valueMap == null) {
            valueMap = new HashMap<String, String>();
            map.set(valueMap);
        }
        if (valueMap.size() <= 10000) {
            valueMap.put(key, value);
        }
    }

    public static void removeThreadVariable(String key) {
        removeThreadVariable(key, threadVariableMap);
        if (isRpcVariableKey(key)) {
            removeThreadVariable(key, threadRpcVariableMap);
        }
    }

    private static void removeThreadVariable(String key,
                                             ThreadLocal<Map<String, String>> map) {
        Map<String, String> valueMap = map.get();
        if (valueMap != null) {
            valueMap.remove(key);
            if (valueMap.isEmpty()) {
                map.set(null);
            }
        }
    }

    public static Map<String, String> getThreadVariableMap() {
        if (threadVariableMap.get() == null) {
            return new HashMap<String, String>();
        }
        return new HashMap<String, String>(threadVariableMap.get());
    }

    public static Map<String, String> getThreadRpcVariableMap() {
        if (threadRpcVariableMap.get() == null) {
            return new HashMap<String, String>();
        }
        return new HashMap<String, String>(threadRpcVariableMap.get());
    }

    public static LogLevel getThreadLogLevel() {
        return threadLogLevel.get();
    }

    public static void setThreadLogLevel(LogLevel logLevel) {
        threadLogLevel.set(logLevel);
    }

    public static void clearThreadLocal() {
        if (threadVariableMap.get() != null) {
            threadVariableMap.get().clear();
        }
        threadVariableMap.set(null);
        if (threadRpcVariableMap.get() != null) {
            threadRpcVariableMap.get().clear();
        }
        threadRpcVariableMap.set(null);
        threadLogLevel.set(null);
    }

    /**
     * 移除保存在日志工厂类中的日志对象
     */
    public static void clearAllLoggers() {
        loggers.clear();
        MDC.clear();
        clearThreadLocal();
        getGlobalMdcMap().clear();
    }

    public static void putRpcVariableKey(String key) {
        if (!rpcVariableKeyList.contains(key)) {
            rpcVariableKeyList.add(key);
        }
    }

    private static boolean isRpcVariableKey(String key) {
        return rpcVariableKeyList.contains(key);
    }

    public ILoggerFactory getILoggerFactory() {
        return org.slf4j.LoggerFactory.getILoggerFactory();
    }
}
