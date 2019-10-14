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
package org.tinygroup.cepcore.util;

import org.tinygroup.context.Context;

import java.util.HashMap;
import java.util.Map;

public class ThreadContextUtil {
    //	private final static ThreadLocal<Context> context = new ThreadLocal<Context>();
    public final static String THREAD_CONTEXT_KEY = "tiny_sys_service_thread_context";
    public final static String LOGGER_THREADLOCAL_KEY = "logger_threadlocal_key";
    private static ThreadLocal<Map<String, Object>> threadVariableMap = new ThreadLocal<Map<String, Object>>() {
    };
//    private static InheritableThreadLocal<Map<String, Object>> threadVariableMap = new InheritableThreadLocal<Map<String, Object>>() {
//        protected Map<String, Object> childValue(
//                Map<String, Object> parentValue) {
//            if (parentValue == null) {
//                return null;
//            }
//            return new HashMap<String, Object>(parentValue);
//        }
//    };

    /**
     * 获取当前上下文context
     *
     * @return
     */
    private static Map<String, Object> getCurrentThreadContext() {
        Map<String, Object> currentcontext = threadVariableMap.get();
        if (currentcontext == null) {
            currentcontext = new HashMap<String, Object>();
            threadVariableMap.set(currentcontext);
        }
        return currentcontext;
    }

    public static Object get(String name) {
        return getCurrentThreadContext().get(name);
    }

    public static void put(String name, Object value) {
        getCurrentThreadContext().put(name, value);
    }

    public static Object remove(String name) {
        Map<String, Object> currentThreadContext = getCurrentThreadContext();
        Object o = currentThreadContext.remove(name);
        if (currentThreadContext.isEmpty()) {
            clear();
        }
        return o;
    }

    /**
     * 清除上下文变量
     */
    public static void clear() {
        getCurrentThreadContext().clear();
        threadVariableMap.set(null);
    }

    /**
     * 将线程变量取出clone一份放入上下文，key为THREAD_CONTEXT_KEY
     *
     * @param mainContext
     */
    public static void putCurrentThreadContextIntoContext(Context mainContext) {
        Map<String, Object> currentContext = getCurrentThreadContext();
        mainContext.put(THREAD_CONTEXT_KEY, new HashMap<String, Object>(currentContext));
    }

    /**
     * 从上下文中解出THREAD_CONTEXT_KEY，并设置为线程变量
     *
     * @param mainContext
     */
    public static void parseCurrentThreadContext(Context mainContext) {
        if (!mainContext.exist(THREAD_CONTEXT_KEY)) {
            return;
        }
        Map<String, Object> currentContext = mainContext.remove(THREAD_CONTEXT_KEY);
//		Context currentContext = mainContext.getSubContext(THREAD_CONTEXT_KEY);
        if (currentContext != null) {
            threadVariableMap.set(currentContext);
        }
    }
}