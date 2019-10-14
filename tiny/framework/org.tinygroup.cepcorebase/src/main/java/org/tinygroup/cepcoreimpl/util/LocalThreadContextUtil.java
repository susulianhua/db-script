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
package org.tinygroup.cepcoreimpl.util;

import org.tinygroup.cepcore.util.ThreadContextUtil;
import org.tinygroup.context.Context;
import org.tinygroup.event.Event;
import org.tinygroup.logger.LoggerFactory;

import java.util.Map;

public class LocalThreadContextUtil {

    /**
     * 异步服务调用的主线程发起之前
     *
     * @param event
     */
    public static void synBeforeCall(Event event) {
        // 将 日志线程变量 放入 请求线程变量
        ThreadContextUtil.put(ThreadContextUtil.LOGGER_THREADLOCAL_KEY,
                LoggerFactory.getThreadVariableMap());
        Context reuqestContext = event.getServiceRequest().getContext();
        // 将 请求线程变量的clone 放入 请求上下文(context),之所以放clone是因为synBeforeCall/synAfterCall与执行线程是异步的
        ThreadContextUtil.putCurrentThreadContextIntoContext(reuqestContext);
    }

    /**
     * 异步服务调用的主线程发起之后
     *
     * @param event
     */
    public static void synAfterCall(Event event) {
        ThreadContextUtil.remove(ThreadContextUtil.LOGGER_THREADLOCAL_KEY);
    }

    /**
     * 异步服务执行线程执行之前
     *
     * @param event
     */
    public static void synBeforeProcess(Event event) {
        Context reuqestContext = event
                .getServiceRequest().getContext();
        //从请求上下文中解析线程上下文
        ThreadContextUtil.parseCurrentThreadContext(reuqestContext);
        parseLoggerThreadLocal();
    }

    private static void parseLoggerThreadLocal() {
        if (ThreadContextUtil.get(ThreadContextUtil.LOGGER_THREADLOCAL_KEY) != null) {
            Map<String, String> loggerThreadLocal = (Map<String, String>) ThreadContextUtil.get(ThreadContextUtil.LOGGER_THREADLOCAL_KEY);
            for (String key : loggerThreadLocal.keySet()) {
                LoggerFactory.putThreadVariable(key, loggerThreadLocal.get(key));
            }
        }
    }

    public static void synAfterProcess() {
        ThreadContextUtil.clear();
        LoggerFactory.clearThreadLocal();
    }
}
