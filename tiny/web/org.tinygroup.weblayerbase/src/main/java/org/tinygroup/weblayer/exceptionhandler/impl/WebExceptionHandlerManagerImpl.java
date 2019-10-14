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
package org.tinygroup.weblayer.exceptionhandler.impl;

import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.exceptionhandler.WebExceptionHandler;
import org.tinygroup.weblayer.exceptionhandler.WebExceptionHandlerManager;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能说明:web异常处理管理接口实现
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-9-22 <br>
 * <br>
 */
public class WebExceptionHandlerManagerImpl implements
        WebExceptionHandlerManager {
    private Map<String, WebExceptionHandler> handlerNameMap = new HashMap<String, WebExceptionHandler>();
    private Map<Class<?>, WebExceptionHandler> handlerMap = new HashMap<Class<?>, WebExceptionHandler>();
    private List<Class<?>> exceptionList = new ArrayList<Class<?>>();
    private WebExceptionHandler defaultHandler = new DefaultWebExceptionHandler();

    public void addHandler(String exception, WebExceptionHandler handler)
            throws ClassNotFoundException {
        if (handlerNameMap.containsKey(exception)) {
            return;
        }
        handlerNameMap.put(exception, handler);
        Class<?> exceptionClass = Class.forName(exception);
        exceptionList.add(exceptionClass);
        handlerMap.put(exceptionClass, handler);

    }

    public boolean handler(Throwable e, WebContext webContext)
            throws IOException, ServletException {
        boolean isProcessed = exceptionHandler(e, webContext);
        if (isProcessed) {
            return true;
        }
        Throwable t = e.getCause();
        while (t != null) {
            isProcessed = exceptionHandler(t, webContext);
            if (isProcessed) {
                return true;
            }
            t = t.getCause();
        }
        if (defaultHandler != null) {
            defaultHandler.handler(e, webContext);
            return true;
        }
        return false;
    }

    private boolean exceptionHandler(Throwable e, WebContext webContext)
            throws IOException, ServletException {
        Class<?> exceptionClass = e.getClass();
        int index = exceptionList.indexOf(exceptionClass);
        if (index != -1) {
            handlerMap.get(exceptionList.get(index)).handler(e, webContext);
            return true;
        }
        for (int i = 0; i < exceptionList.size(); i++) {
            Class<?> clazz = exceptionList.get(i);
            if (implmentInterface(exceptionClass, clazz)) {
                handlerMap.get(clazz).handler(e, webContext);
                return true;
            }
        }
        return false;
    }

    private boolean implmentInterface(Class<?> clazz, Class<?> interfaceClazz) {
        return interfaceClazz.isAssignableFrom(clazz);
    }

    public void setDefaultHandler(WebExceptionHandler handler) {
        this.defaultHandler = handler;
    }

}
